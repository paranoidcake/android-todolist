package com.example.todolist

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.lifecycle.Observer
import com.example.todolist.data.*
import com.example.todolist.fragment.dialog.adddialog.AddTodoDialogFragment
import com.example.todolist.fragment.dialog.editdialog.EditTodoDialogFragment
import com.example.todolist.fragment.todoitem.TodoItemFragment

class MainActivity : AppCompatActivity() {

    private val addTodoDialogFragment = AddTodoDialogFragment()
    private var editTodoDialogFragment = EditTodoDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todoStateObserver =
            Observer<Int> { newState -> run {
                when(newState) {                                      // If there's a better way I'd rather use it, but this works fine with no more than 1 update per state change while staying readable
                    STATE_FETCHED -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        TodoHandler.todoList.forEach {
                            id, todo -> run {
                                fragmentTransaction.add(R.id.todoList, TodoItemFragment(id, todo), id.toString())
                            }
                        }
                        fragmentTransaction.commit()
                    }

                    STATE_ADDED -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        val id = TodoHandler.getLastId()
                        val itemFragment = TodoItemFragment(id, TodoHandler.todoList[id])

                        fragmentTransaction.add(R.id.todoList, itemFragment, id.toString()).commit()
                    }

                    STATE_CLEARED -> {
                        for(fragment in supportFragmentManager.fragments) {
                            supportFragmentManager.beginTransaction().remove(fragment).commit()
                        }
                    }

                    else -> {}
                }
            } }

        TodoHandler.state.observe(this, todoStateObserver)

        TodoHandler.fetchTodoList(applicationContext) // Should probably remove persistence (uses base64 encoding to avoid whitespace/newline issues, FileInputStream, so on)
    }

    fun addTodoBtn(view: View) {
        val title =
            addTodoDialogFragment.view?.findViewById<EditText>(R.id.addDialogTitle)

        val description =
            addTodoDialogFragment.view?.findViewById<EditText>(R.id.addDialogDescription)

        if(title?.text.toString() == "") {                                      // Input validation, not sure we need to include this
            title?.error = "You must enter a title"
        } else {
            TodoHandler.addTodo(Todo(title?.text.toString(), description?.text.toString()), applicationContext)

            title?.setText("")
            description?.setText("")
            addTodoDialogFragment.dismiss()
        }
    }

    fun openAddDialogBtn(view: View) = addTodoDialogFragment.show(supportFragmentManager, "add_todo")

    fun editTodoBtn(view: View) {
        val parentView = (view.parent as View)
        val title =
            parentView.findViewById<TextView>(R.id.itemTitle).text.toString()

        val description =
            parentView.findViewById<TextView>(R.id.itemDescription).text.toString()

        val id = parentView.tag.toString()

        openEditDialog(id, title, description)
    }

    fun editTodoDialogBtn(view: View) {
        val parentView = (view.parent as View)

        val todoId = editTodoDialogFragment.todoId.toInt()

        val newTitle = parentView.findViewById<EditText>(R.id.editDialogTitle).text.toString()
        val newDescription = parentView.findViewById<EditText>(R.id.editDialogDescription).text.toString()

        println(todoId)
        val fragment = supportFragmentManager.findFragmentByTag(todoId.toString())

        fragment?.view?.findViewById<TextView>(R.id.itemTitle)?.text = newTitle
        fragment?.view?.findViewById<TextView>(R.id.itemDescription)?.text = newDescription

        TodoHandler.todoList[todoId] = Todo(newTitle, newDescription)
        TodoHandler.saveTodoList(applicationContext)

        editTodoDialogFragment.dismiss()
    }

    private fun openEditDialog(id: String, title: String, description: String) {
        editTodoDialogFragment = EditTodoDialogFragment(id, title, description)

        editTodoDialogFragment.show(supportFragmentManager, "edit_todo")
    }


    fun clearAllBtn(view: View) = TodoHandler.clearTodoList(applicationContext)

    fun deleteTodoBtn(view: View) {
        val parentTag = (view.parent as View).tag.toString()
        val fragment = supportFragmentManager.findFragmentByTag(parentTag)

        if(fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()

            TodoHandler.todoList.removeAt(parentTag.toInt())
            TodoHandler.saveTodoList(applicationContext)
        } else {
            println("Fragment not found!")
        }
    }
}
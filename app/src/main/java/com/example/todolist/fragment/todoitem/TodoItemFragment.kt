package com.example.todolist.fragment.todoitem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.todolist.R
import com.example.todolist.data.Todo

class TodoItemFragment(private val todoId: Int, todo: Todo): Fragment() {

//    private val viewModel: TodoItemViewModel by viewModels()

    private val title = todo.title
    private val description = todo.description

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflaterView = inflater.inflate(R.layout.todo_item_fragment, container, false)

        inflaterView.tag = todoId.toString()
        inflaterView.findViewById<TextView>(R.id.itemTitle).text = title
        inflaterView.findViewById<TextView>(R.id.itemDescription).text = description

        return inflaterView
    }
}

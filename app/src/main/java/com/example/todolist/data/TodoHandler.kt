package com.example.todolist.data

import android.content.Context
import android.util.Base64
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.lifecycle.MutableLiveData
import java.io.FileNotFoundException
import java.nio.charset.Charset

const val STATE_FETCHED = 0
const val STATE_ADDED = 1
const val STATE_CLEARED = 2

object TodoHandler {

    val todoList = SparseArray<Todo>() // I think SparseArray would be better for this use case, but might be confusing to explain; It's interchangeable with HashMap, which could be smoother
    private val idList = ArrayList<Int>()

    val state: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun saveTodoList(context: Context) {
        val todoFile = context.openFileOutput("todo.txt", 0).bufferedWriter()

        todoFile.write("")
        todoFile.close()

        todoList.forEach { id, todo -> run {
            appendTodoFile(id, todo, context)
        } }
    }

    fun addTodo(todo: Todo, context: Context) {
        if(idList.isEmpty()) {
            appendTodoFile(0, todo, context)

            todoList[0] = todo
            idList.add(0)
        } else {
            val nextId = idList.last()+1

            appendTodoFile(nextId, todo, context)

            todoList[nextId] = todo
            idList.add(nextId)
        }

        state.value = STATE_ADDED
    }

    fun clearTodoList(context: Context) {
        todoList.clear()
        idList.clear()

        val todoFile = context.openFileOutput("todo.txt", 0).bufferedWriter()

        todoFile.write("")
        todoFile.close()

        state.value = STATE_CLEARED
    }

    fun getLastId(): Int {
        return idList.last()
    }

    fun fetchTodoList(context: Context) {
        try {
            deserializeFile(context)

            println("todoList: $todoList")
        } catch (e: FileNotFoundException) {
            println("todo.txt not found")
        }

        state.value = STATE_FETCHED
    }

    private fun appendTodoFile(id: Int, todo: Todo, context: Context) {
        val todoFile = context.openFileOutput("todo.txt", Context.MODE_APPEND).bufferedWriter()

        todoFile.write("$id;${todo.serialize()}")
        todoFile.newLine()
        todoFile.close()
    }

    private fun getTodoFromEncoded(encodedTitle: String, encodedDescription: String): Todo {
        return Todo(
            Base64.decode(encodedTitle, Base64.NO_WRAP).toString(Charset.defaultCharset()),
            Base64.decode(encodedDescription, Base64.NO_WRAP).toString(Charset.defaultCharset())
        )
    }

    private fun deserialize(encodedString: String): Pair<Int, Todo> {
        val input = encodedString.split(";")

        return Pair(input[0].toInt(), getTodoFromEncoded(input[1], input[2]))
    }

    private fun deserializeFile(context: Context) {
        val todoFile = context.openFileInput("todo.txt").bufferedReader()

        todoFile.forEachLine {
            if(it != "") {
                val (id, todo) = deserialize(it)
                idList.add(id)
                todoList[id] = todo
            }
        }
        todoFile.close()
    }
}
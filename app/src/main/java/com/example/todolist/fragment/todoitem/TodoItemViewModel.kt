package com.example.todolist.fragment.todoitem

import androidx.lifecycle.ViewModel
import com.example.todolist.data.Todo

class TodoItemViewModel: ViewModel() {

    lateinit var title: String
    lateinit var description: String

    fun init(todo: Todo) {
        title = todo.title
        description = todo.description
    }
}
package com.example.todolist.data

import android.util.Base64

data class Todo (val title: String, val description: String) {

    fun serialize(delimiter: String = ";"): String {
        return Base64.encodeToString(title.toByteArray(), Base64.NO_WRAP) + delimiter + Base64.encodeToString(description.toByteArray(), Base64.NO_WRAP)
    }

}
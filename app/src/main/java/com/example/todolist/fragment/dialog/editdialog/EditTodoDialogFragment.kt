package com.example.todolist.fragment.dialog.editdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.todolist.R

class EditTodoDialogFragment (val todoId: String = "", private val title: String = "", private val description: String = "") : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflaterView = inflater.inflate(R.layout.dialog_fragment_edit_todo, container, false)

        inflaterView.tag = id
        inflaterView.findViewById<EditText>(R.id.editDialogTitle).setText(title)
        inflaterView.findViewById<EditText>(R.id.editDialogDescription).setText(description)

        return inflaterView
    }

    override fun onStop() {
        view?.findViewById<EditText>(R.id.editDialogTitle)?.setText("")
        view?.findViewById<EditText>(R.id.editDialogDescription)?.setText("")

        super.onStop()
    }
}

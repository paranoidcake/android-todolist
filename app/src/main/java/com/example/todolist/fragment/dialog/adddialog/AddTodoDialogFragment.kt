package com.example.todolist.fragment.dialog.adddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.todolist.R

class AddTodoDialogFragment : DialogFragment() {

//    private val viewModel: AddTodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_add_todo, container, false)
    }

    override fun onStop() {
        view?.findViewById<EditText>(R.id.addDialogTitle)?.setText("")
        view?.findViewById<EditText>(R.id.addDialogDescription)?.setText("")

        super.onStop()
    }
}

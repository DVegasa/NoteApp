package io.github.dvegasa.todoapp.screens.note_edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.dvegasa.todoapp.R

const val ARG_NOTE_ID = "id"

class NoteEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
    }
}

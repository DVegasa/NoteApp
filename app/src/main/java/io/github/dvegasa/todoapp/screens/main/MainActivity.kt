package io.github.dvegasa.todoapp.screens.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.dvegasa.todoapp.data_models.Note
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), MainContract.View {

    private var presenter: MainContract.Presenter? = MainPresenter(this)
    private lateinit var adapter: RvNotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(io.github.dvegasa.todoapp.R.style.AppTheme)
        setContentView(io.github.dvegasa.todoapp.R.layout.activity_main)

        presenter?.viewIsReady()
    }

    private fun initRvNotes(notes: List<Note>) {
        Log.d("ed", "initRvNotes()")
        adapter = RvNotesAdapter(notes as ArrayList<Note>, presenter!!)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            rvNotes.context,
            (rvNotes.layoutManager as LinearLayoutManager).orientation
        )
        rvNotes.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }

    override fun showNotes(notes: List<Note>) {
        initRvNotes(notes)
    }

    override fun onNoteRemoved() {
    }

    override fun editNote(note: Note) {
        Toast.makeText(this, "editNote", Toast.LENGTH_LONG).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}

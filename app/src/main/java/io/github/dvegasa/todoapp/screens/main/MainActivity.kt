package io.github.dvegasa.todoapp.screens.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.FakeData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RvNotesAdapter
    private val storage = FakeData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        initToolbar()
        storage.getAllNotes (object : FakeData.Callback {
            override fun onResult(list: ArrayList<Note>) {
                initRvNotes(list)
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Записи"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return true
    }

    private fun initRvNotes(notes: List<Note>) {
        Log.d("ed", "initRvNotes()")
        adapter = RvNotesAdapter(notes as ArrayList<Note>)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            rvNotes.context,
            (rvNotes.layoutManager as LinearLayoutManager).orientation
        )
        rvNotes.addItemDecoration(dividerItemDecoration)
    }

}

package io.github.dvegasa.todoapp.screens.attachments

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.FakeData
import kotlinx.android.synthetic.main.activity_attachments.*
import org.jetbrains.anko.toast

const val ARG_NOTE_ID = "id"

class AttachmentsActivity : AppCompatActivity() {

    private var noteId: Long = -1
    private lateinit var note: Note

    private val storage = FakeData()

    private lateinit var adapter: RvAttachmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attachments)
        initToolbar()
        getNoteId()
        loadNote()
        initRvAttachments()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Прикреплённые файлы"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getNoteId() {
        intent.extras.let {
            noteId = it?.getLong(ARG_NOTE_ID) ?: -1
        }
    }

    private fun loadNote() {
        if (noteId < 0) {
            toast("Ошибка загрузки")
            finish()
            return
        }
        storage.getNoteById(noteId, object : FakeData.Callback {
            override fun onResult(list: ArrayList<Note>) {
                note = list[0]
            }
        })
    }

    private fun initRvAttachments() {
        val list = note.attachments
        adapter = RvAttachmentsAdapter(list)
        rvAttachments.layoutManager = LinearLayoutManager(this)
        rvAttachments.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}

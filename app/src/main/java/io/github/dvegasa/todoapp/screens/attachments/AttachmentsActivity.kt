package io.github.dvegasa.todoapp.screens.attachments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.NoteStorageInterface
import io.github.dvegasa.todoapp.storage.file_worker.FileConverter
import io.github.dvegasa.todoapp.storage.room_sql.RoomStorage
import io.github.dvegasa.todoapp.utils.PathUtils
import kotlinx.android.synthetic.main.activity_attachments.*
import org.jetbrains.anko.toast

const val ARG_NOTE_ID = "id"
const val RQ_FILE_PICKER = 10001
const val PERM_RQ_READ_MEMORY = 20001

class AttachmentsActivity : AppCompatActivity() {

    private var noteId: Long = -1
    private lateinit var note: Note

    private val storage = RoomStorage()
    private val fileConverter = FileConverter()

    private lateinit var adapter: RvAttachmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.github.dvegasa.todoapp.R.layout.activity_attachments)
        initToolbar()
        getNoteId()
        initViews()
        loadNote()
        setBtnAddAttachmentsEnabled(false)
        checkForPermissions()
    }

    private fun checkForPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Чтобы добавить файл нужно предоставить разрешение доступа к памяти")
                    .setPositiveButton("Разрешить") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERM_RQ_READ_MEMORY
                        )

                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERM_RQ_READ_MEMORY
                )
            }
        } else {
            setBtnAddAttachmentsEnabled(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERM_RQ_READ_MEMORY -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setBtnAddAttachmentsEnabled(true)
                } else {
                    setBtnAddAttachmentsEnabled(false)
                }
                return
            }

            else -> {
                // Ignore all other requests
            }
        }
    }

    private fun setBtnAddAttachmentsEnabled(enabled: Boolean) {
        val textOn = "Добавить файл"
        val textOff = "Чтобы добавить файл нужно предоставить разрешение доступа к памяти"
        ivPlus.visibility = if (enabled) View.VISIBLE else View.GONE
        tvAddFileText.text = if (enabled) textOn else textOff
        llAddAttachment.isEnabled = enabled
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

    private fun initViews() {
        llAddAttachment.setOnClickListener {
            showFilePicker()
        }
    }

    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, RQ_FILE_PICKER)
    }

    private fun loadNote() {
        if (noteId < 0) {
            toast("Ошибка загрузки")
            finish()
            return
        }
        storage.getNoteById(noteId, object : NoteStorageInterface.Callback {
            override fun onResult(results: ArrayList<Note>?) {
                note = results!![0]
                initRvAttachments()
            }
        })
    }

    private fun initRvAttachments() {
        val list = ArrayList<FileInfo>()
        note.attachments.forEach {
            list.add(fileConverter.uriToFileInfo(it))
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RQ_FILE_PICKER -> {
                    val uri = data?.data
                    Log.d("ed", " Uri: $uri")
                    prepareFileBeforeSending(uri)
                }
            }
        }
    }

    private fun prepareFileBeforeSending(uri: Uri?) {
        if (uri == null) {
            toast("Не удалось загрузить изображение")
            return
        }
        val path = PathUtils.getPath(this, uri)
        Log.d("ed", "Path: $path")
    }
}

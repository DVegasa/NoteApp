package io.github.dvegasa.todoapp.screens.note_edit

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.screens.attachments.ARG_NOTE_ID
import io.github.dvegasa.todoapp.screens.attachments.AttachmentsActivity
import io.github.dvegasa.todoapp.storage.NoteStorageInterface
import io.github.dvegasa.todoapp.storage.room_sql.RoomStorage
import io.github.dvegasa.todoapp.utils.NoteHelper
import io.github.dvegasa.todoapp.utils.SystemUtils
import kotlinx.android.synthetic.main.activity_note_edit.*
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


const val ARG_NOTE_ID = "id"

class NoteEditActivity : AppCompatActivity(), ToolbarAndMenuManagerNE.Callback {

    private var note: Note? = null
    private val storage: NoteStorageInterface = RoomStorage()

    private var toolbarMenuManager: ToolbarAndMenuManagerNE? = null

    private var isTagsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        toolbarMenuManager = ToolbarAndMenuManagerNE(this).apply {
            init(toolbar)
            setAttachmentsNumber(note?.attachments?.size ?: 0)
            setTagIconEnabled(false)
        }

        val id = intent.extras?.getLong(ARG_NOTE_ID) ?: 0
        loadNote(id)
        initViews()
    }

    override fun onPause() {
        saveNote()
        super.onPause()
    }

    private fun initViews() {
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() ?: true) {
                    etTitle.typeface = Typeface.DEFAULT
                } else {
                    etTitle.typeface = Typeface.DEFAULT_BOLD
                }
            }
        })

    }

    private fun loadNote(id: Long) {
        storage.getNoteById(id, object : NoteStorageInterface.Callback {
            override fun onResult(results: ArrayList<Note>?) {
                note = results!![0]
                Log.d(
                    "ed__",
                    "NoteEditActivity.loadNote(): note.isLocked = ${note?.isLocked.toString()}"
                )
                showNote()
                setReadOnlyEnabled(note?.isLocked ?: false, callOnInit = true)
                toolbarMenuManager?.setLockIconEnabled(note?.isLocked ?: false)
            }
        })
    }

    private fun showNote() {
        etTitle.setText(note?.title)
        etBody.setText(note?.body)
        etTags.setText(note?.tagsToString())
    }

    @Suppress("UNREACHABLE_CODE")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbarMenuManager?.onCreateOptionsMenu(menu) ?: true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toolbarMenuManager?.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun saveNote(closeActivity: Boolean) {
        writeNoteToDb()
        if (closeActivity) {
            finish()
        }
    }

    override fun shareNote() {
        val text = "${note?.title}\n\n${note?.body}\n\n${note?.tagsToString()}"
        share(text)
    }

    override fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Подтвердите удаление")
            .setMessage(etTitle.text)
            .setPositiveButton("Удалить") { dialog, which ->
                storage.deleteNote(note!!.id, object : NoteStorageInterface.Callback {
                    override fun onResult(results: ArrayList<Note>?) {
                        toast("Заметка удалена")
                        finish()
                    }
                })
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    override fun showAttachments() {
        startActivity<AttachmentsActivity>(ARG_NOTE_ID to note?.id)
    }

    override fun switchTagsVisibility() {
        isTagsVisible = !isTagsVisible
        etTags.visibility = if (isTagsVisible) View.VISIBLE else View.GONE

        if (isTagsVisible) {
            val a = Toast.makeText(
                this,
                "Для разделения тегов используйте символ решётки #",
                Toast.LENGTH_SHORT
            )
            a.setGravity(Gravity.TOP, 0, 22)
            a.show()
        }
        toolbarMenuManager?.setTagIconEnabled(isTagsVisible)
    }

    override fun switchNoteLockedStatus() {
        note?.isLocked = !note?.isLocked!!
        setReadOnlyEnabled(note?.isLocked!!, callOnInit = false)
        toolbarMenuManager?.setLockIconEnabled(note?.isLocked ?: false)
    }

    private fun setReadOnlyEnabled(b: Boolean, callOnInit: Boolean) {
        val edits = listOf<EditText>(etTitle, etTags, etBody)
        Log.d("ed__", "NoteEditActivity.setNoteReadOnlyEnabled: calledOnInit=$callOnInit")
        edits.forEach {
            it.isFocusable = !b
            it.isFocusableInTouchMode = !b
            it.clearFocus()
        }

        if (b) {
            toast("Только чтение")
        } else {
            if (!callOnInit) toast("Можно редактировать")
        }
    }

    private fun writeNoteToDb() {
        note?.title = etTitle.text.toString()
        note?.body = etBody.text.toString()
        note?.lastTimeModified = SystemUtils.getCurrentTime()
        note?.tags = NoteHelper.tagStringToList(etTags.text.toString())
        storage.updateNote(note!!, object : NoteStorageInterface.Callback {
            override fun onResult(results: ArrayList<Note>?) {
                Log.d("ed__", "NoteEditActivity.saveNote(): Note is saved")
            }
        })
    }
}

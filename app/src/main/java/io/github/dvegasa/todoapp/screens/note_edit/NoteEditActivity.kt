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
import androidx.core.content.res.ResourcesCompat
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

class NoteEditActivity : AppCompatActivity() {

    private var note: Note? = null
    private val storage: NoteStorageInterface = RoomStorage()
    private var isTagsShown: Boolean = false
    private var menu: Menu? = null
        set(value) {
            setNoteReadOnlyEnabled(note?.isLocked ?: false, true)
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        val id = intent.extras?.getLong(ARG_NOTE_ID) ?: 0
        initToolbar()
        initViews()
        loadNote(id)
    }

    override fun onPause() {
        saveNote()
        super.onPause()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
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
                showNote()
            }
        })
    }

    private fun showNote() {
        etTitle.setText(note?.title)
        etBody.setText(note?.body)
        etTags.setText(note?.tagsToString())
        // setNoteReadOnlyEnabled(note.isLocked, true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_screen_menu, menu)
        this.menu = menu
        menu?.findItem(R.id.action_tags)?.setOnMenuItemClickListener {
            isTagsShown = !isTagsShown
            if (isTagsShown) {
                menu.findItem(R.id.action_tags).icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_sharp_activated, null)
            } else {
                menu.findItem(R.id.action_tags).icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_sharp_normal, null)
            }

            setTagsEnabled()

            false
        }

        menu?.findItem(R.id.action_lock)?.setOnMenuItemClickListener {
            note?.isLocked = !note?.isLocked!!
            setNoteReadOnlyEnabled(note?.isLocked ?: false, false)
            false
        }

        val icon = when (note?.attachments?.size) {
            0 -> R.drawable.ic_attach_file_toolbar_black_24dp
            1 -> R.drawable.ic_attachment_number_1
            2 -> R.drawable.ic_attachment_number_2
            3 -> R.drawable.ic_attachment_number_3
            4 -> R.drawable.ic_attachment_number_4
            5 -> R.drawable.ic_attachment_number_5
            6 -> R.drawable.ic_attachment_number_6
            7 -> R.drawable.ic_attachment_number_7
            8 -> R.drawable.ic_attachment_number_8
            9 -> R.drawable.ic_attachment_number_9
            else -> R.drawable.ic_attachment_number_9_plus
        }

        menu?.findItem(R.id.action_attachments)?.icon =
            ResourcesCompat.getDrawable(resources, icon, null)

        if (note?.isLocked == true) {
            menu?.findItem(R.id.action_lock)?.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_activated, null)
        } else {
            menu?.findItem(R.id.action_lock)?.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_normal, null)
        }
        return true
    }

    private fun setNoteReadOnlyEnabled(isReadOnly: Boolean, calledOnInit: Boolean) {
        val edits = listOf<EditText>(etTitle, etTags, etBody)
        Log.d("ed__", "NoteEditActivity.setNoteReadOnlyEnabled: calledOnInit=$calledOnInit")
        edits.forEach {
            it.isFocusable = !isReadOnly
            it.isFocusableInTouchMode = !isReadOnly
            it.clearFocus()
        }

        if (isReadOnly) {
            menu?.findItem(R.id.action_lock)?.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_activated, null)
            toast("Только чтение")
        } else {
            menu?.findItem(R.id.action_lock)?.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_normal, null)
            if (!calledOnInit) toast("Можно редактировать")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                saveNote()
                finish()
            }
            R.id.action_share -> {
                val text = "${note?.title}\n\n${note?.body}\n\n${note?.tagsToString()}"
                share(text)
            }
            R.id.action_attachments -> {
                startActivity<AttachmentsActivity>(ARG_NOTE_ID to note?.id)
            }
            R.id.action_delete -> {
                showDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
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

    private fun setTagsEnabled() {
        etTags.visibility = if (isTagsShown) View.VISIBLE else View.GONE

        if (isTagsShown) {
            val a = Toast.makeText(
                this,
                "Для разделения тегов используйте символ решётки #",
                Toast.LENGTH_SHORT
            )
            a.setGravity(Gravity.TOP, 0, 22)
            a.show()
        }
    }

    private fun saveNote() {
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

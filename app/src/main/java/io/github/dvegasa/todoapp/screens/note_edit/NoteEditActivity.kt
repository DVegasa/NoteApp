package io.github.dvegasa.todoapp.screens.note_edit

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.FakeData
import kotlinx.android.synthetic.main.activity_note_edit.*
import org.jetbrains.anko.share
import org.jetbrains.anko.toast


const val ARG_NOTE_ID = "id"

class NoteEditActivity : AppCompatActivity() {

    private lateinit var note: Note
    private val storage = FakeData()
    private var isTagsShown: Boolean = false
    private var isReadOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        val id = intent.extras?.getLong(ARG_NOTE_ID) ?: 0
        initToolbar()
        initViews()
        loadNote(id)
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
        storage.getNoteById(id, object : FakeData.Callback {
            override fun onResult(list: ArrayList<Note>) {
                note = list[0]
                showNote()
            }
        })
    }

    private fun showNote() {
        etTitle.setText(note.title)
        etBody.setText(note.body)
        etTags.setText(note.tagsToString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_screen_menu, menu)
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
            isReadOnly = !isReadOnly
            val edits = listOf<EditText>(etTitle, etTags, etBody)

            edits.forEach {
                it.isFocusable = !isReadOnly
                it.isFocusableInTouchMode = !isReadOnly
                it.clearFocus()
            }

            if (isReadOnly) {
                menu.findItem(R.id.action_lock).icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_activated, null)
                toast("Только чтение")
            } else {
                menu.findItem(R.id.action_lock).icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_normal, null)
                toast("Можно редактировать")
            }

            false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.action_share -> {
                val text = "${note.title}\n\n${note.body}\n\n${note.tagsToString()}"
                share(text)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTagsEnabled() {
        etTags.visibility = if (isTagsShown) View.VISIBLE else View.GONE

        if (isTagsShown) {
            val a = Toast.makeText(this, "Для разделения тегов используйте символ решётки #", Toast.LENGTH_SHORT)
            a.setGravity(Gravity.TOP, 0, 22)
            a.show()
        }
    }
}

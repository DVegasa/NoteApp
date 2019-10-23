package io.github.dvegasa.todoapp.screens.main

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.screens.main.rv_notes.RvNotesManager
import io.github.dvegasa.todoapp.screens.main.rv_notes.SORT_NEW_FIRST
import io.github.dvegasa.todoapp.screens.main.rv_notes.SORT_OLD_FIRST
import io.github.dvegasa.todoapp.screens.main.rv_notes.SORT_TITLE
import io.github.dvegasa.todoapp.screens.note_edit.ARG_NOTE_ID
import io.github.dvegasa.todoapp.screens.note_edit.NoteEditActivity
import io.github.dvegasa.todoapp.screens.preferences.PREF_KEY_PREVIEW_LIMIT
import io.github.dvegasa.todoapp.screens.preferences.SettingsActivity
import io.github.dvegasa.todoapp.storage.NoteStorageInterface
import io.github.dvegasa.todoapp.storage.room_sql.RoomStorage
import io.github.dvegasa.todoapp.storage.shared_pref.UserPreferences
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_sort.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), RvNotesManager.Callback {

    private val storage: NoteStorageInterface = RoomStorage()
    private var rvNotesManager: RvNotesManager? = null
    private var toolbarMenu: Menu? = null
    private val dialog by lazy {
        BottomSheetDialog(this)
    }

    private val userPref by lazy {
        UserPreferences(this)
    }

    private val sharedPrefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                PREF_KEY_PREVIEW_LIMIT -> rvNotesManager?.onNotePreviewLimitChanged()
            }
        }

    private val sortHandler = View.OnClickListener { view ->
        when (view.id) {
            R.id.tvSortByTitle -> rvNotesManager?.sortNotes(SORT_TITLE)
            R.id.tvSortNewFirst -> rvNotesManager?.sortNotes(SORT_NEW_FIRST)
            R.id.tvSortOldFirst -> rvNotesManager?.sortNotes(SORT_OLD_FIRST)
        }
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        rvNotesManager = RvNotesManager(this, rvNotes, storage)
        rvNotesManager?.init()
        initToolbar()
        initFab()
        initSearchComponent()
    }

    override fun onResume() {
        super.onResume()
        userPref.sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener)
        rvNotesManager?.updateData()
    }

    override fun onPause() {
        super.onPause()
        userPref.sharedPref.unregisterOnSharedPreferenceChangeListener(sharedPrefListener)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Записи"
    }

    private fun initFab() {
        fabCreateNote.setOnClickListener {
            Log.d("ed__", "MainActivity.initFab(): fab clicked")
            storage.createNote(object : NoteStorageInterface.Callback {
                override fun onResult(results: ArrayList<Note>?) {
                    // Открыть только что созданную заметку
                    Log.d(
                        "ed__",
                        "MainActivity.initFab(): result received, opening new activity..."
                    )
                    rvNotesManager?.updateData()
                    startActivity<NoteEditActivity>(ARG_NOTE_ID to results!![0].id)
                }
            })
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toolbarMenu = menu
        menuInflater.inflate(R.menu.main_screen_menu, toolbarMenu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> showSortDialog()
            R.id.action_settings -> showSettings()
            R.id.action_delete_notes -> showMultipleDeletionDialog()
        }

        return true
    }

    private fun showMultipleDeletionDialog() {
        val selected = rvNotesManager?.getListOfSelected() ?: arrayListOf<Long>()
        val n = selected.size

        AlertDialog.Builder(this)
            .setTitle("Подтвердите удаление")
            .setMessage("Будет удалено заметок: $n")
            .setPositiveButton("Удалить") { dialog, which ->
                rvNotesManager?.setClickable(false)

                storage.deleteNotes(selected, object : NoteStorageInterface.Callback {
                    override fun onResult(results: ArrayList<Note>?) {
                        rvNotesManager?.onNotesDeleted()
                        rvNotesManager?.setClickable(true)
                        toast("Заметки удалены")
                    }
                })
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }.show()
    }


    private fun initSearchComponent() {
        etSearchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rvNotesManager?.handleSearchEvent(s.toString())
            }
        })
    }

    override fun isInSearchMode(): Boolean = etSearchField.text.isNotEmpty()

    override fun setDeletionModeEnabled(b: Boolean) {
        Log.d("ed__", "MainActivity.setDeletionModeEnabled(): $b")
        toolbarMenu?.findItem(R.id.action_delete_notes)?.isVisible = b
        toolbarMenu?.findItem(R.id.action_settings)?.isVisible = !b
        toolbarMenu?.findItem(R.id.action_sort)?.isVisible = !b
    }

    private fun showSortDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_sort, null)
        view.tvSortByTitle.setOnClickListener(sortHandler)
        view.tvSortNewFirst.setOnClickListener(sortHandler)
        view.tvSortOldFirst.setOnClickListener(sortHandler)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSettings() {
        startActivity<SettingsActivity>()
    }
}
package io.github.dvegasa.todoapp.screens.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
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


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RvNotesAdapter
    private val storage: NoteStorageInterface = RoomStorage()
    private val dialog by lazy {
        BottomSheetDialog(this)
    }

    private val userPref by lazy {
        UserPreferences(
            this
        )
    }

    private val sharedPrefListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            PREF_KEY_PREVIEW_LIMIT -> adapter.notifyDataSetChanged()
        }
    }


    private val sortHandler = View.OnClickListener { view ->
        when (view.id) {
            R.id.tvSortByTitle -> {
                adapter.list.sortBy {
                    it.title
                }
                adapter.notifyDataSetChanged()
            }

            R.id.tvSortNewFirst -> {
                adapter.list.sortByDescending {
                    it.lastTimeModified
                }
                adapter.notifyDataSetChanged()
            }

            R.id.tvSortOldFirst -> {
                adapter.list.sortBy {
                    it.lastTimeModified
                }
                adapter.notifyDataSetChanged()

            }
        }
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        initToolbar()
        initFab()
        initRvNotes()
        updateNotes()
    }

    override fun onResume() {
        super.onResume()
        userPref.sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener)
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
                    Log.d("ed__", "MainActivity.initFab(): result received, opening new activity...")
                    updateNotes()
                    startActivity<NoteEditActivity>(ARG_NOTE_ID to results!![0].id)
                }
            })
        }
    }

    private fun updateNotes() {
        storage.getAllNotes(object : NoteStorageInterface.Callback {
            override fun onResult(results: ArrayList<Note>?) {
                updateRvNotesAdapterList(results!!)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Поиск"
        searchView.maxWidth = android.R.attr.width

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                Log.d("ed", "expand")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                Log.d("ed", "Collapse")
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = visible
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> showSortDialog()
            R.id.action_settings -> showSettings()
        }

        return true
    }

    private fun initRvNotes() {
        Log.d("ed", "initRvNotes()")
        adapter = RvNotesAdapter(arrayListOf())
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            rvNotes.context,
            (rvNotes.layoutManager as LinearLayoutManager).orientation
        )
        rvNotes.addItemDecoration(dividerItemDecoration)
    }

    private fun updateRvNotesAdapterList(notes: ArrayList<Note>) {
        adapter.list = notes
        adapter.notifyDataSetChanged()
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
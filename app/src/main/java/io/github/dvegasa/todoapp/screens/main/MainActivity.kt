package io.github.dvegasa.todoapp.screens.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import io.github.dvegasa.todoapp.storage.RealmStorage
import io.github.dvegasa.todoapp.storage.UserPreferences
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RvNotesAdapterNew
    private val storage = RealmStorage()
    private val dialog by lazy {
        BottomSheetDialog(this)
    }

    private val userPref by lazy { UserPreferences(this) }

    private val sharedPrefListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            PREF_KEY_PREVIEW_LIMIT -> adapter.notifyDataSetChanged()
        }
    }


//    private val sortHandler = View.OnClickListener { view ->
//        when (view.id) {
//            R.id.tvSortByTitle -> {
//                adapter.list.sortBy {
//                    it.title
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            R.id.tvSortNewFirst -> {
//                adapter.list.sortByDescending {
//                    it.lastTimeModified
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            R.id.tvSortOldFirst -> {
//                adapter.list.sortBy {
//                    it.lastTimeModified
//                }
//                adapter.notifyDataSetChanged()
//
//            }
//        }
//        dialog.dismiss()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        initToolbar()
        initFabListener()
        loadData()
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

    private fun initFabListener() {
        fabCreate.setOnClickListener {
            val noteId = RealmStorage().create()
            startActivity<NoteEditActivity>(ARG_NOTE_ID to noteId)
        }
    }

    private fun loadData() {
        initRvNotes(storage.getAllNotes())
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

    private fun initRvNotes(notes: List<Note>) {
        Log.d("ed", "initRvNotes()")
        adapter = RvNotesAdapterNew(Realm.getDefaultInstance().where<Note>().findAll())
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            rvNotes.context,
            (rvNotes.layoutManager as LinearLayoutManager).orientation
        )
        rvNotes.addItemDecoration(dividerItemDecoration)
    }

    private fun showSortDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_sort, null)
//        view.tvSortByTitle.setOnClickListener(sortHandler)
//        view.tvSortNewFirst.setOnClickListener(sortHandler)
//        view.tvSortOldFirst.setOnClickListener(sortHandler)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSettings() {
        startActivity<SettingsActivity>()
    }
}
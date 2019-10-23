package io.github.dvegasa.todoapp.screens.main

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.github.dvegasa.todoapp.R

/**
 * 23.10.2019
 */

const val TOOLBAR_TITLE = "Заметки"


class ToolbarAndMenuManager(
    private val context: Context
) {
    private val hostActivity = context as AppCompatActivity
    private var toolbarMenu: Menu? = null
    private val callback: Callback = context as Callback

    fun init(toolbar: Toolbar?) {
        hostActivity.setSupportActionBar(toolbar)
        hostActivity.supportActionBar?.title = TOOLBAR_TITLE
    }

    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toolbarMenu = menu
        hostActivity.menuInflater.inflate(R.menu.main_screen_menu, menu)
        return true
    }

    fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> callback.showSortDialog()
            R.id.action_settings -> callback.showSettings()
            R.id.action_delete_notes -> callback.showMultipleDeletionDialog()
        }
        return true
    }

    fun setToolbarDeletitionModeEnabled(b: Boolean) {
        toolbarMenu?.findItem(R.id.action_delete_notes)?.isVisible = b
        toolbarMenu?.findItem(R.id.action_settings)?.isVisible = !b
        toolbarMenu?.findItem(R.id.action_sort)?.isVisible = !b
    }

    interface Callback {
        fun showSortDialog()
        fun showSettings()
        fun showMultipleDeletionDialog()
    }
}
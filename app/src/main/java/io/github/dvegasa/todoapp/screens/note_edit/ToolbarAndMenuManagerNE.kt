package io.github.dvegasa.todoapp.screens.note_edit

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import io.github.dvegasa.todoapp.R

/**
 * 23.10.2019
 */
class ToolbarAndMenuManagerNE(
    context: Context
) {
    private val hostActivity = context as AppCompatActivity
    private val callback = context as Callback
    private var toolbarMenu: Menu? = null
    private var lockIcon = R.drawable.ic_lock_normal

    fun init(toolbar: Toolbar) {
        hostActivity.setSupportActionBar(toolbar)
        hostActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        hostActivity.supportActionBar?.title = ""
    }

    fun setLockIconEnabled(b: Boolean) {
        lockIcon = if (b) R.drawable.ic_lock_activated
        else R.drawable.ic_lock_normal

        toolbarMenu?.findItem(R.id.action_lock)?.icon =
            ResourcesCompat.getDrawable(
                hostActivity.resources,
                lockIcon,
                null
            )
    }

    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        hostActivity.menuInflater.inflate(R.menu.edit_note_screen_menu, menu)
        toolbarMenu = menu

        menu?.findItem(R.id.action_lock)?.icon =
            ResourcesCompat.getDrawable(
                hostActivity.resources,
                lockIcon,
                null
            )

        menu?.findItem(R.id.action_tags)?.setOnMenuItemClickListener {
            callback.switchTagsVisibility()
            false
        }

        menu?.findItem(R.id.action_lock)?.setOnMenuItemClickListener {
            callback.switchNoteLockedStatus()
            false
        }
        return true
    }

    fun setTagIconEnabled(b: Boolean) {
        if (b) {
            toolbarMenu?.findItem(R.id.action_tags)?.icon =
                ResourcesCompat.getDrawable(
                    hostActivity.resources,
                    R.drawable.ic_sharp_activated,
                    null
                )
        } else {
            toolbarMenu?.findItem(R.id.action_tags)?.icon =
                ResourcesCompat.getDrawable(
                    hostActivity.resources,
                    R.drawable.ic_sharp_normal,
                    null
                )
        }
    }

    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            android.R.id.home -> {
                callback.saveNote(closeActivity = true)
            }
            R.id.action_share -> {
                callback.shareNote()
            }
            R.id.action_delete -> {
                callback.showDeleteDialog()
            }
        }
    }


    interface Callback {
        fun switchNoteLockedStatus()
        fun saveNote(closeActivity: Boolean = false)
        fun shareNote()
        fun showDeleteDialog()
        fun switchTagsVisibility()
    }

}
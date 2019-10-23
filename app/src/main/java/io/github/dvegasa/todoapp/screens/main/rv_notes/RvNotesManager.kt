package io.github.dvegasa.todoapp.screens.main.rv_notes

import android.content.Context
import android.util.Log
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.NoteStorageInterface

/**
 * 23.10.2019
 */
class RvNotesManager(
    private val context: Context,
    private val rvNotes: RecyclerView,
    private val storage: NoteStorageInterface
) {

    private val activityHost: Callback = context as Callback
    private var adapter = RvNotesAdapter(arrayListOf())
    private var tracker: SelectionTracker<Long>? = null

    fun init() {
        adapter = RvNotesAdapter(arrayListOf())
        rvNotes.layoutManager = LinearLayoutManager(context)
        rvNotes.adapter = adapter

        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            rvNotes,
            MyItemKeyProvider(rvNotes),
            MyItemDetailsLookup(rvNotes),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {

            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val isPickingEnabled =
                    !(tracker == null || tracker!!.selection == null || tracker!!.selection.isEmpty)

                if (adapter.isPickingMode != isPickingEnabled) {
                    activityHost.setDeletionModeEnabled(isPickingEnabled)
                }
                adapter.isPickingMode = isPickingEnabled
            }
        })

        adapter.tracker = tracker

        val dividerItemDecoration = DividerItemDecoration(
            rvNotes.context,
            (rvNotes.layoutManager as LinearLayoutManager).orientation
        )
        rvNotes.addItemDecoration(dividerItemDecoration)
    }

    fun updateData() {
        if (!activityHost.isInSearchMode()) {
            storage.getAllNotes(object : NoteStorageInterface.Callback {
                override fun onResult(results: ArrayList<Note>?) {
                    adapter.updateList(results!!)
                }
            })
        }
    }

    fun handleSearchEvent(q: String) {
        adapter.filter.filter(q)
    }

    fun sortNotes(criteria: Int) {
        adapter.doSort(criteria)
    }

    fun onNotePreviewLimitChanged() {
        adapter.notifyDataSetChanged()
    }

    fun onNotesDeleted() {
        updateData()
        tracker?.clearSelection()
    }

    fun getListOfSelected(): ArrayList<Long> {
        val selection = tracker?.selection
        Log.d(
            "ed__",
            "RvNotesManager.getListOfSelected(): selection size = ${selection?.size() ?: "null"}"
        )
        return ArrayList(selection?.toList() ?: emptyList<Long>())
    }

    fun setClickable(b: Boolean) {
        Log.d("ed__", "RvNotesManager.setClickable(): fired with $b")
        adapter.isElementsClickable = b
    }

    interface Callback {
        fun setDeletionModeEnabled(b: Boolean)
        fun isInSearchMode(): Boolean
    }
}
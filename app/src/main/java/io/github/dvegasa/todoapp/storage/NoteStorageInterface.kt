package io.github.dvegasa.todoapp.storage

import android.util.Log
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 17.10.2019
 */
interface NoteStorageInterface {
    interface Callback {
        fun onResult(results: ArrayList<Note>?)
        fun onFailure(ex: Exception) {
            Log.d("ed__", ex.toString())
        }
    }

    fun getAllNotes(cb: Callback)

    fun createNote(cb: Callback)

    fun getNoteById(id: Long, cb: Callback)

    fun updateNote(note: Note, cb: Callback)

    fun deleteNote(id: Long, cb: Callback)
}



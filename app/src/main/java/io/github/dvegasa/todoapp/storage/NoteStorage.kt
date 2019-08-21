package io.github.dvegasa.todoapp.storage

/**
 * 21.08.2019
 */
interface NoteStorage {

    fun getNoteById(id: Long, c: Callback)
    fun getAllNotes(c: Callback)

    interface Callback {
        fun onResult(o: Any)
        fun onFailure(o: Any)
    }
}
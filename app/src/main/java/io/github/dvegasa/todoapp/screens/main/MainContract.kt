package io.github.dvegasa.todoapp.screens.main

import io.github.dvegasa.todoapp.data_models.Note

/**
 * 20.08.2019
 */
interface MainContract {
    interface View {
        fun showNotes(notes: List<Note>)
        fun onNoteRemoved()
        fun editNote(note: Note)
        fun showError(error: String)
    }

    interface Presenter {
        fun onSearch(q: String)
        fun onNoteClicking(note: Note)
        fun onNoteRemoving(note: Note)
        fun onNoteCreating()

        fun viewIsReady()
        fun onDestroy()
    }
}
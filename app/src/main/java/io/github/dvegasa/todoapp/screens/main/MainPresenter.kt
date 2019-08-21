package io.github.dvegasa.todoapp.screens.main

import android.util.Log
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.FakeData
import io.github.dvegasa.todoapp.storage.NoteStorage

/**
 * 21.08.2019
 */
class MainPresenter(var view: MainContract.View?) : MainContract.Presenter {

    private var storage: NoteStorage = FakeData()

    @Suppress("UNCHECKED_CAST")
    override fun viewIsReady() {
        storage.getAllNotes(object : NoteStorage.Callback {
            override fun onResult(o: Any) {
                Log.d("ed", "presenter.viewIsReady().onResult()")
                view?.showNotes(o as List<Note>)
            }

            override fun onFailure(o: Any) {
                view?.showError("failed")
            }
        })
    }

    override fun onDestroy() {
        view = null
    }

    override fun onNoteClicking(note: Note) {
        storage.getNoteById(note.id, object : NoteStorage.Callback {
            override fun onResult(o: Any) {
                @Suppress("UNCHECKED_CAST")
                view?.editNote((o as List<Note>)[0])
            }

            override fun onFailure(o: Any) {

            }
        })
    }


    override fun onSearch(q: String) {

    }



    override fun onNoteRemoving(note: Note) {

    }

    override fun onNoteCreating() {

    }
}
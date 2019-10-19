package io.github.dvegasa.todoapp.storage.room_sql

import android.os.Handler
import android.util.Log
import io.github.dvegasa.todoapp.MyApplication
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.NoteStorageInterface

/**
 * 18.10.2019
 */


class RoomStorage() : NoteStorageInterface {

    val dao = MyApplication.appDatabase.noteDao()
    val h = Handler()

    override fun getAllNotes(cb: NoteStorageInterface.Callback) {
        Thread(Runnable {
            val l = dao.getAllNotes()
            h.post {
                cb.onResult(l as ArrayList<Note>)
            }
        }).start()
    }

    override fun createNote(cb: NoteStorageInterface.Callback) {
        Thread(Runnable {
            Log.d("ed__", "RoomStorage.createNote(): get request for adding")

            // Создаём новую заметку и получаем её id
            val id = dao.insertNote(Note().apply {
                title = (1000..9999).random().toString()
            })
            Log.d("ed__", "RoomStorage.createNote(): id received = $id")
            Log.d("ed__", "RoomStorage.createNote(): requesting for object Note...")

            // Получаем объект этой заметки
            getNoteById(id, object : NoteStorageInterface.Callback {
                override fun onResult(results: ArrayList<Note>?) {
                    Log.d("ed__", "RoomStorage.createNote(): note received. Sending to UI thread..")
                    h.post {
                        // Отправляем объект только что созданной заметки в UI поток
                        cb.onResult(arrayListOf(results!![0]))
                    }
                }
            })
        }).start()
    }

    override fun getNoteById(id: Long, cb: NoteStorageInterface.Callback) {
        Thread(Runnable {
            val list = dao.getNoteById(id)
            h.post {
                if (list.isNotEmpty()) cb.onResult(ArrayList(list))
                else cb.onFailure(Exception("There is no element with id $id"))
            }
        }).start()
    }

    override fun updateNote(note: Note, cb: NoteStorageInterface.Callback) {
        Thread(Runnable {
            dao.updateNote(note)
            h.post {
                cb.onResult(null)
            }
        }).start()
    }

    override fun deleteNote(id: Long, cb: NoteStorageInterface.Callback) {
        Thread(Runnable {
            dao.deleteNote(id)
            h.post {
                cb.onResult(null)
            }
        }).start()
    }

}
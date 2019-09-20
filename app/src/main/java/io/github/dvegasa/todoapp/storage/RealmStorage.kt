package io.github.dvegasa.todoapp.storage

import io.github.dvegasa.todoapp.data_models.Note
import io.realm.Realm
import io.realm.kotlin.where

/**
 * 19.09.2019
 */
class RealmStorage {
    val realm: Realm = Realm.getDefaultInstance()

    // return note.id
    fun create(): Long {
        realm.beginTransaction()
        val note = realm.createObject(Note::class.java, getActualId())
        realm.commitTransaction()
        return note.id
    }

    fun getNoteById(id: Long) = realm.where<Note>()
        .equalTo("id", id)
        .findFirst() !!

    fun getAllNotes(): List<Note> {
        val results = realm.where<Note>()
            .findAll()

        val list = ArrayList<Note>()
        list.addAll(realm.copyFromRealm(results))
        return list
    }

    private fun getActualId() = System.currentTimeMillis()
}
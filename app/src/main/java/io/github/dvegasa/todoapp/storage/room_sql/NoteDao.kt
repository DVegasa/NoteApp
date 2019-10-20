package io.github.dvegasa.todoapp.storage.room_sql

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 18.10.2019
 */

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Long): List<Note>

    /**
     * @return id of created Note
     */
    @Insert
    fun insertNote(note: Note): Long

    @Update
    fun updateNote(note: Note)

    @Query("DELETE FROM note WHERE id = :id")
    fun deleteNote(id: Long)

    @Query("DELETE FROM note WHERE id IN (:ids)")
    fun deleteNotes(ids: Array<Long>)
}
package io.github.dvegasa.todoapp.storage.room_sql

import androidx.room.*
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 18.10.2019
 */

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Insert
    fun insertNote(note: Note): Long

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Long): List<Note>

    @Update
    fun updateNote(note: Note): Int

    @Delete
    fun deleteNote(note: Note): Int
}
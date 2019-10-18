package io.github.dvegasa.todoapp.storage.room_sql

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 18.10.2019
 */

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
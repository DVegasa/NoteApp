package io.github.dvegasa.todoapp

import android.app.Application
import android.util.Log
import androidx.room.Room
import io.github.dvegasa.todoapp.storage.room_sql.AppDatabase

/**
 * 18.10.2019
 */

class MyApplication : Application() {

    // Почему нельзя получить этот объект из любого другого класса??
    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ed__", "onCreate() application")
        appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "note.appDatabase"
        ).build()
    }

}
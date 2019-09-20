package io.github.dvegasa.todoapp

import android.app.Application
import io.realm.Realm

/**
 * 19.09.2019
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
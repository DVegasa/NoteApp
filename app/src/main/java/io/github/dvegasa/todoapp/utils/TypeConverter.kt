package io.github.dvegasa.todoapp.utils

import androidx.room.TypeConverter
import java.util.*


/**
 * 18.10.2019
 */

object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun listToString(list: List<String>): String {
        var s = ""
        list.forEach {
            s += it +"\n"
        }
        return s
    }

    @TypeConverter
    @JvmStatic
    fun stringToList(string: String): List<String> {
        val list = ArrayList<String>()
        val sc = Scanner(string)
        while (sc.hasNextLine()) {
            list.add(sc.nextLine())
        }
        return list
    }
}

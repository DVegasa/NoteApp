package io.github.dvegasa.todoapp

import io.github.dvegasa.todoapp.utils.TypeConverter
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TypeConverterTest {
    @Test
    fun stringToList() {
        assertEquals(
            arrayListOf("a", "b", "c"),
            TypeConverter.stringToList("a\nb\nc\n")
        )

        assertEquals(
            arrayListOf("a1", "b2", "c3"),
            TypeConverter.stringToList("a1\nb2\nc3\n")
        )

        assertEquals(
            arrayListOf("", ""),
            TypeConverter.stringToList("\n\n")
        )

        assertEquals(
            arrayListOf<String>(),
            TypeConverter.stringToList("")
        )

        assertEquals(
            arrayListOf("a1", "b2", ""),
            TypeConverter.stringToList("a1\nb2\n\n")
        )
    }

    @Test
    fun listToString() {
        assertEquals(
            "meow\ngau\n",
            TypeConverter.listToString(arrayListOf("meow", "gau"))
        )

        assertEquals(
            "a1\nb2\n\n",
            TypeConverter.listToString(arrayListOf("a1", "b2", ""))
        )

        assertEquals(
            "",
            TypeConverter.listToString(arrayListOf())
        )
    }
}

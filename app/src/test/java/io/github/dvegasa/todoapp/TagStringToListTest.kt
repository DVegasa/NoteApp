package io.github.dvegasa.todoapp

import io.github.dvegasa.todoapp.utils.NoteHelper
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 19.10.2019
 */
class TagStringToListTest {

    val t = NoteHelper

    @Test
    fun test() {
        assertEquals(
            arrayListOf("meow", "gau"),
            t.tagStringToList("#meow #gau")
        )

        assertEquals(
            arrayListOf("meow", "gau42"),
            t.tagStringToList("#meow     #gau   42")
        )

        assertEquals(
            arrayListOf("sample"),
            t.tagStringToList("sample")
        )
    }
}
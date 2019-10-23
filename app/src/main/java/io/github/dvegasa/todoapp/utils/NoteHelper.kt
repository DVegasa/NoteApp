package io.github.dvegasa.todoapp.utils

import io.github.dvegasa.todoapp.data_models.Note

/**
 * 31.08.2019
 */
class NoteHelper {
    companion object {
        fun tagsPreview(note: Note): String {
            if (note.tags.isEmpty()) return ""
            var a = ""

            var usedTags = 0

            for (tag in note.tags) {
                if (a.length > 25) break
                a = "$a #$tag"
                usedTags++
            }
            if (usedTags != note.tags.size) {
                var leftTags = note.tags.size - usedTags
                a = "$a +$leftTags"
            }
            return a.trim()
        }

        fun tagStringToList(tags_: String): ArrayList<String> {
            var tags = tags_.replace(" ", "", true)
            val list = arrayListOf<String>()
            var t = ""
            for (i in tags.indices) {
                if (tags[i] == '#') {
                    if (i != 0) {
                        list.add(t)
                    }
                    t = ""
                } else {
                    t += tags[i]
                }
            }
            list.add(t)
            return list
        }
    }
}
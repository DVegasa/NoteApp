package io.github.dvegasa.todoapp.data_models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.github.dvegasa.todoapp.utils.SystemUtils
import io.github.dvegasa.todoapp.utils.TypeConverter

/**
 * 20.08.2019
 */
@Entity
@TypeConverters(TypeConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String = "",
    var body: String = "",
    var tags: List<String> = arrayListOf(),
    var isLocked: Boolean = false,
    var lastTimeModified: Long = SystemUtils.getCurrentTime()
) {
    @Suppress("LocalVariableName")
    fun tagsToString(): String {
        if (tags.isEmpty()) return ""
        var tags_ = ""
        this.tags.forEachIndexed { index, s ->
            tags_ = "$tags_ #$s"
        }
        tags_ = tags_.trim()
        Log.d("ed__", "tags_ = $tags_")
        if (tags_.length == 1)
            tags_ = ""
        return tags_
    }
}
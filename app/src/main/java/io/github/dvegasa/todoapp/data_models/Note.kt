package io.github.dvegasa.todoapp.data_models

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
    var lastTimeModified: Long = SystemUtils.getCurrentTime(),
    var attachments: List<String> = arrayListOf()
) {
    @Suppress("LocalVariableName")
    fun tagsToString(): String {
        var tags_ = ""
        this.tags.forEachIndexed { index, s ->
            tags_ = "$tags_ #$s"
        }
        return tags_.trim()
    }
}
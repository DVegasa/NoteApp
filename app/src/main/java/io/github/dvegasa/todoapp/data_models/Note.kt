package io.github.dvegasa.todoapp.data_models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * 20.08.2019
 */
open class Note(
    var title: String = "",
    var body: String = "",
    @PrimaryKey var id: Long = 0L,
    var tags: RealmList<String> = RealmList<String>(),
    var lastTimeModified: Long = 0L,
    var attachments: RealmList<FileInfo> = RealmList<FileInfo>()

) : RealmObject() {

    @Suppress("LocalVariableName")
    fun tagsToString(): String {
        var tags_ = ""
        this.tags.forEachIndexed { index, s ->
            tags_ = "$tags_ #$s"
        }
        return tags_.trim()
    }
}
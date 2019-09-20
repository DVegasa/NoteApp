package io.github.dvegasa.todoapp.data_models

import io.realm.RealmObject

/**
 * 20.08.2019
 */
open class FileInfo (
    var title: String = "",
    var size: Float = 0f,
    var format: String = "",
    var previewUri: String = "",
    var downloadUri: String = ""
) : RealmObject()
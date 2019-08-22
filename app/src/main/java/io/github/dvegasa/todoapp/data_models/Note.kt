package io.github.dvegasa.todoapp.data_models

/**
 * 20.08.2019
 */
data class Note(
    var title: String,
    var body: String,
    val id: Long,
    var tags: List<String>,
    var lastTimeModified: Long,
    var attachments: List<FileInfo>
)
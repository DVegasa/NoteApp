package io.github.dvegasa.todoapp.data_models

/**
 * 20.08.2019
 */
data class FileInfo(
    val title: String,
    val size: Float,
    val format: String,
    val previewUri: String,
    val downloadUri: String
)
package io.github.dvegasa.todoapp.storage.file_worker

import io.github.dvegasa.todoapp.data_models.FileInfo

/**
 * 18.10.2019
 */
class FileConverter {
    fun uriToFileInfo(uri: String): FileInfo {
        return FileInfo("sample", 0.0f, "txt", "none", "none")
    }
}
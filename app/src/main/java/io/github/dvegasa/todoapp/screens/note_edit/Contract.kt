package io.github.dvegasa.todoapp.screens.note_edit

import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 20.08.2019
 */
interface Contract {
    interface View {
        fun showNote(note: Note)
        fun attachmentRemoved()
        fun onNoteRemoved()
    }

    interface Presenter {
        fun onAttachmentRemove(note: Note, removableAttachment: FileInfo)
        fun onAttachmentDownload(note: Note, downloadableAttachment: FileInfo)
        fun updateNote(note: Note)
        fun removeNote(note: Note)
    }
}
package io.github.dvegasa.todoapp.screens.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.screens.note_edit.ARG_NOTE_ID
import io.github.dvegasa.todoapp.screens.note_edit.NoteEditActivity
import io.github.dvegasa.todoapp.storage.UserPreferences
import io.github.dvegasa.todoapp.utils.NoteHelper
import io.realm.*
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.item_note.view.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * 20.09.2019
 */
class RvNotesAdapterNew(var data_: OrderedRealmCollection<Note>) :
    RealmRecyclerViewAdapter<Note, RvNotesAdapterNew.VH>(data_, true),
    Filterable {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    val customFilter = RvNotesAdapterNew.CustomFilter(this, data_.toList() as ArrayList<Note>)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(io.github.dvegasa.todoapp.R.layout.item_note, parent, false)
        return RvNotesAdapterNew.VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.apply {

            val note = getItem(position)!!

            setOnClickListener {
                context.startActivity<NoteEditActivity>(ARG_NOTE_ID to note.id)
            }

            val limit = UserPreferences(context).getPreviewBodySymbolsLimit()
            tvBody.text = if (note.body.length < limit) {
                note.body
            } else if (note.body.isEmpty()) {
                "Empty note"
            } else {
                "${note.body.subSequence(0, limit)}..."
            }

            tvDate.text = parseTime(note.lastTimeModified)
            tvHeader.text = if (note.title.isNotEmpty()) note.title else "No topic"

            tvTags.text = NoteHelper.tagsPreview(note)

            if (note.attachments.isEmpty()) {
                llAttachments.visibility = View.INVISIBLE
            } else {
                llAttachments.visibility = View.VISIBLE
                tvAttachmentsNumber.text = note.attachments.size.toString()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseTime(millis_: Long): String {
        val millis = millis_ * 1000
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    override fun getFilter() = customFilter

    private fun filterResult(query: CharSequence?) {
        val realm = Realm.getDefaultInstance()
        val realmQuery: RealmQuery<Note> = realm.where<Note>()
        if (!(query == null || query.isEmpty())) {
            realmQuery.contains("title", query.toString(), Case.INSENSITIVE)
                .or()
                .contains("body", query.toString(), Case.INSENSITIVE)
                .or()
                .contains("tags", query.toString(), Case.INSENSITIVE)
        }
        updateData(realmQuery.findAllAsync())
    }

    class CustomFilter(val adapter: RvNotesAdapterNew, val filterList: ArrayList<Note>) : Filter() {

        override fun performFiltering(query: CharSequence?): FilterResults {
            return FilterResults()

        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            adapter.filterResult(constraint)
        }

    }

}
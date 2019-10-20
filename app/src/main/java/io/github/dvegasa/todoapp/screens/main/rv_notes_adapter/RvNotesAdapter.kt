package io.github.dvegasa.todoapp.screens.main.rv_notes_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.screens.note_edit.ARG_NOTE_ID
import io.github.dvegasa.todoapp.screens.note_edit.NoteEditActivity
import io.github.dvegasa.todoapp.storage.shared_pref.UserPreferences
import io.github.dvegasa.todoapp.utils.NoteHelper
import kotlinx.android.synthetic.main.item_note.view.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * 20.08.2019
 */
class RvNotesAdapter(var list: ArrayList<Note>) : RecyclerView.Adapter<RvNotesAdapter.VH>(),
    Filterable {

    val customFilter = CustomFilter(this, list.clone() as ArrayList<Note>)

    var tracker: SelectionTracker<Long>? = null

    var isPickingMode = false

    init {
        setHasStableIds(true)
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, isPicked: Boolean = false) {
            itemView.apply {
                isActivated = isPicked

                val note = list[position]

                setOnClickListener {
                    if (!isPickingMode) {
                        context.startActivity<NoteEditActivity>(ARG_NOTE_ID to note.id)
                    }
                }

                val limit = UserPreferences(context).getPreviewBodySymbolsLimit()
                tvBody.text = if (note.body.length < limit) {
                    note.body
                } else {
                    "${note.body.subSequence(0, limit)}..."
                }

                tvDate.text = parseTime(note.lastTimeModified)
                tvHeader.text = note.title

                tvTags.text = NoteHelper.tagsPreview(note)

                if (note.attachments.isEmpty()) {
                    llAttachments.visibility = View.INVISIBLE
                } else {
                    llAttachments.visibility = View.VISIBLE
                    tvAttachmentsNumber.text = note.attachments.size.toString()
                }
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(io.github.dvegasa.todoapp.R.layout.item_note, parent, false)
        return VH(view)
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int): Long = list[position].id

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(position, tracker?.isSelected(getItemId(position)) ?: false)
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

    class CustomFilter(val adapter: RvNotesAdapter, val filterList: ArrayList<Note>) : Filter() {

        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()
            if (query != null && query.isNotEmpty()) {

                val filtered = filterList.filter {
                    it.title.contains(query, true) || it.body.contains(
                        query,
                        true
                    ) || it.tags.contains(query)
                } as ArrayList<Note>

                results.count = filtered.size
                results.values = filtered
            } else {
                results.count = filterList.size
                results.values = filterList
            }
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            adapter.list = results?.values as ArrayList<Note>
            adapter.notifyDataSetChanged()
        }

    }
}
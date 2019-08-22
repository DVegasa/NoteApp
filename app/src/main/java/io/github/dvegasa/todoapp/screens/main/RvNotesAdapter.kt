package io.github.dvegasa.todoapp.screens.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.data_models.Note
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 20.08.2019
 */
class RvNotesAdapter(var list: ArrayList<Note>) : RecyclerView.Adapter<RvNotesAdapter.VH>(), Filterable{

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    val customFilter = CustomFilter(this, list.clone() as ArrayList<Note>)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(io.github.dvegasa.todoapp.R.layout.item_note, parent, false)
        return VH(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.apply {

            val note = list[position]

            setOnClickListener {
                // todo
            }

            tvBody.text = if (note.body.length < 50) {
                note.body
            } else {
                "${note.body.subSequence(0, 49)}..."
            }

            tvDate.text = parseTime(note.lastTimeModified)
            tvHeader.text = note.title
            var tags = ""

            note.tags.forEachIndexed { index, s ->
                tags = "$tags #$s"
            }
            tvTags.text = tags

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

    class CustomFilter(val adapter: RvNotesAdapter, val filterList: ArrayList<Note>) : Filter() {

        override fun performFiltering(query: CharSequence?): FilterResults {
            val results = FilterResults()
            if (query != null && query.isNotEmpty()) {

                val filtered = filterList.filter {
                    it.title.contains(query, true) || it.body.contains(query, true) || it.tags.contains(query)
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
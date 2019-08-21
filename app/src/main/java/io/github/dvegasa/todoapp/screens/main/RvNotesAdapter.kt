package io.github.dvegasa.todoapp.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * 20.08.2019
 */
class RvNotesAdapter(var list: ArrayList<Note>) : RecyclerView.Adapter<RvNotesAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
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

            tvDate.text = "15 авг"
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
}
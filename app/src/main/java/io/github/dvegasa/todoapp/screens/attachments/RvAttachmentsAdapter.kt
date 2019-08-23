package io.github.dvegasa.todoapp.screens.attachments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.dvegasa.todoapp.R
import io.github.dvegasa.todoapp.data_models.FileInfo
import kotlinx.android.synthetic.main.item_attachment.view.*

/**
 * 23.08.2019
 */
class RvAttachmentsAdapter(var list: List<FileInfo>) : RecyclerView.Adapter<RvAttachmentsAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attachment, parent, false)

        view.ivRemove.setOnClickListener {
            val dialog = AlertDialog.Builder(parent.context)
                .setTitle("Удалить файл?")
                .setPositiveButton("Удалить") { dialog, which ->
                    // todo
                }
                .setNegativeButton("Не удалять") { dialog, which ->
                    dialog.dismiss()
                }
            dialog.show()
        }


        return VH(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.apply {
            val file = list[position]
            tvTitle.text = file.title
            tvSubtitle.text = "${file.size} MB"
        }
    }
}
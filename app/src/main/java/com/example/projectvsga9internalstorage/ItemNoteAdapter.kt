package com.example.projectvsga9internalstorage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ItemModel(
    val fileName: String,
    val title: String,
    val dateTime: String,
)

class ItemNoteAdapter(
    private val context: Context,
    private val items: List<ItemModel>,
    private val onItemClick: (ItemModel) -> Unit
) : RecyclerView.Adapter<ItemNoteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.title)
        val dateCreatedTextView: TextView = view.findViewById(R.id.dateCreated)

        init {
            view.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.dateCreatedTextView.text = item.dateTime
    }

    override fun getItemCount() = items.size

}


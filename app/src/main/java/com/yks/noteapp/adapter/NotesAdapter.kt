package com.yks.noteapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yks.noteapp.databinding.NoteItemBinding
import com.yks.noteapp.model.Note
import java.text.SimpleDateFormat
import java.util.*


class NotesAdapter: RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): Note {
        return differ.currentList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            titleTxt.text = item.title
            descTxt.text = item.description
            dateTxt.text = timeToDate(item.time)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun timeToDate(milliseconds: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val resultDate = Date(milliseconds)
        return sdf.format(resultDate)
    }


    inner class ViewHolder(val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((Note) -> Unit)? = null

    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }
}
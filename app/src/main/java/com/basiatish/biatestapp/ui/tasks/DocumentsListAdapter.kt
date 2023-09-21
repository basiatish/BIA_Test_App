package com.basiatish.biatestapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.databinding.DocumentsItemBinding
import com.basiatish.biatestapp.ui.tasks.interfaces.OnDeleteButtonClickListener

class DocumentsListAdapter(private val onButtonClickListener: OnDeleteButtonClickListener)
    : ListAdapter<String, DocumentsListAdapter.ViewHolder>(DiffCallBack) {

    inner class ViewHolder(private val binding: DocumentsItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(name: String) {
                binding.name.text = name
                binding.deleteButton.setOnClickListener {
                    onButtonClickListener.onDeleteClick(name)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsListAdapter.ViewHolder {
        val binding = DocumentsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentsListAdapter.ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}
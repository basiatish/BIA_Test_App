package com.basiatish.biatestapp.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.databinding.TaskListItemBinding
import com.basiatish.domain.entities.TaskList

class TaskListAdapter(private val onButtonClickListener: OnButtonClickListener)
    : ListAdapter<TaskList, TaskListAdapter.ViewHolder>(DiffCallBack) {

    inner class ViewHolder(private val binding: TaskListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(task: TaskList) {
                when (task.status) {
                    "Current" -> {
                        binding.taskStatus.visibility = View.VISIBLE
                        binding.orderDetails.text = task.orderDetails
                        binding.payDetails.text = task.payDetails
                    }
                    "Done" -> {
                        binding.taskStatus.visibility = View.GONE
                        binding.detailsContainer.visibility = View.GONE
                        binding.doneTaskContainer.visibility = View.VISIBLE
                    }
                    "Available" -> {
                        binding.taskStatus.visibility = View.GONE
                        binding.orderDetails.text = task.orderDetails
                        binding.payDetails.text = task.payDetails
                    }
                    "Decline" -> {
                        binding.taskStatus.visibility = View.GONE
                        binding.detailsContainer.visibility = View.GONE
                        binding.declineTaskContainer.visibility = View.VISIBLE
                    }
                }
                binding.apply {
                    taskType.text = task.cargoType
                    date.text = task.date
                    time.text = task.time
                    startRoute.text = task.startPoint
                    endRoute.text = task.endPoint
                }

                binding.showDetailsButton.setOnClickListener {
                    onButtonClickListener.onViewDetailsClick(task.id)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.ViewHolder {
        val binding = TaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<TaskList>() {
        override fun areItemsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
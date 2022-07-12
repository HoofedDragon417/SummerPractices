package com.example.summerpractics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.summerpractics.R
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.databinding.RecyclerviewItemTaskBinding
import com.example.summerpractics.models.TaskDataModel

class TaskAdapter(
    private val context: Context, private val items: ArrayList<TaskDataModel>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: RecyclerviewItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val item = items[position]

            binding.priorityTextview.text = item.priority.toString()
            binding.durationTextview.text =
                context.getString(R.string.tool_hint_duration).format(item.duration)
            binding.titleTextview.text = item.title
            binding.noteTextview.text = item.note

            when (item.completed) {

                0 -> binding.taskCompleted.isChecked = false
                else -> binding.taskCompleted.isChecked = true

            }

            binding.taskCompleted.setOnClickListener {

                when (binding.taskCompleted.isChecked) {

                    true -> {

                        item.completed = 1
                        DataBaseHelper(context).updateTasks(item)

                    }

                    else -> {

                        item.completed = 0
                        DataBaseHelper(context).updateTasks(item)

                    }

                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        return TaskViewHolder(
            RecyclerviewItemTaskBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}
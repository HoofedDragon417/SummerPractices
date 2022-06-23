package com.example.summerpractics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.summerpractics.databinding.RecyclerviewItemMeetingBinding
import com.example.summerpractics.models.MeetingDataModel
import java.text.SimpleDateFormat

class MeetingAdapter(
    private val context: Context,
    private val items: ArrayList<MeetingDataModel>
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    inner class MeetingViewHolder(private val binding: RecyclerviewItemMeetingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = items[position]

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")

            binding.meetingTitle.text = item.title
            binding.meetingNote.text = item.note
            binding.beginTime.text = sdf.format(item.beginTime)
            binding.endTime.text = sdf.format(item.endTime)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {

        return MeetingViewHolder(
            RecyclerviewItemMeetingBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {

        return items.size

    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {

        holder.bind(position)

    }

}
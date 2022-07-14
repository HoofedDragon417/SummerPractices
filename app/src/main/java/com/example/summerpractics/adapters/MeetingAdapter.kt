package com.example.summerpractics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.summerpractics.R
import com.example.summerpractics.databinding.RecyclerviewItemMeetingBinding
import com.example.summerpractics.models.MeetingDataModel
import java.text.SimpleDateFormat
import java.util.*

class MeetingAdapter(
    private val context: Context,
    private val items: ArrayList<MeetingDataModel>
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    inner class MeetingViewHolder(private val binding: RecyclerviewItemMeetingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = items[position]

            val pattern = context.getString(R.string.meeting_pattern)
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())

            binding.meetingTitle.text = item.title
            binding.meetingNote.text = item.note
            binding.beginTime.text = sdf.format(item.duration.periodBegin)
            binding.endTime.text = sdf.format(item.duration.periodEnd)
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
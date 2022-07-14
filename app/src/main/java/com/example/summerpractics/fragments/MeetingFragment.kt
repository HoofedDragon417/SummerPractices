package com.example.summerpractics.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.adapters.MeetingAdapter
import com.example.summerpractics.databinding.FragmentMeetingBinding
import com.example.summerpractics.models.DatePeriodModel
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.storage.SharedPreference
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MeetingFragment : Fragment() {

    private var _binding: FragmentMeetingBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (SharedPreference(requireContext()).getInt(THEME_ID)) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.meetingImageviewToolbar.setImageResource(R.drawable.ic_baseline_calendar_month_white_24)
            AppCompatDelegate.MODE_NIGHT_NO -> binding.meetingImageviewToolbar.setImageResource(R.drawable.ic_baseline_calendar_month_black_24)
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM ->
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> binding.meetingImageviewToolbar.setImageResource(
                        R.drawable.ic_baseline_calendar_month_white_24
                    )
                    Configuration.UI_MODE_NIGHT_NO -> binding.meetingImageviewToolbar.setImageResource(
                        R.drawable.ic_baseline_calendar_month_black_24
                    )
                }
        }

        val datePattern = requireContext().getString(R.string.date_format)
        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())

        val dates = SharedPreference(requireContext()).getDate()

        showListOfMeetings(dates, sdf)

        binding.meetingImageviewToolbar.setOnClickListener {

            var calendar = Calendar.getInstance()
            calendar = setZeros(calendar)

            val dateInMillis = calendar.timeInMillis

            val constraints = CalendarConstraints.Builder().setOpenAt(dateInMillis).build()

            val datePicker =
                MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraints).build()

            datePicker.addOnPositiveButtonClickListener {
                val dateToday = Date(it)
                val dateTomorrow = Date(it)
                dateTomorrow.date += 1

                showListOfMeetings(DatePeriodModel(dateToday.time, dateTomorrow.time), sdf)
            }

            datePicker.show(this.parentFragmentManager, "select date")
        }

    }

    private fun showListOfMeetings(dates: DatePeriodModel, sdf: SimpleDateFormat) {
        val listOfMeetings = DataBaseHelper(context).viewMeetings(dates)

        binding.meetingTextviewToolbar.text = sdf.format(dates.periodBegin)

        binding.recyclerMeetingFragment.adapter =
            MeetingAdapter(requireContext(), listOfMeetings)
    }

    private fun setZeros(date: Calendar): Calendar {
        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0

        return date
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() =
            MeetingFragment()
    }
}
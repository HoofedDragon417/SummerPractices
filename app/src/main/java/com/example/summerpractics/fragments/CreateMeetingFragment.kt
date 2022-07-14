package com.example.summerpractics.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.databinding.FragmentCreateMeetingBinding
import com.example.summerpractics.models.DatePeriodModel
import com.example.summerpractics.models.MeetingDataModel
import com.example.summerpractics.storage.DataBaseHelper
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.text.SimpleDateFormat
import java.util.*

class CreateMeetingFragment : Fragment() {

    private var _binding: FragmentCreateMeetingBinding? = null
    private val binding get() = requireNotNull(_binding)

    companion object {

        var temporaryStart = Date()
        var temporaryEnd = Date()

        fun newInstance() =
            CreateMeetingFragment()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMeetingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePattern = requireContext().getString(R.string.date_format)
        val timePattern = requireContext().getString(R.string.time_format)

        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
        val stf = SimpleDateFormat(timePattern, Locale.getDefault())

        binding.startMeetingTextview.setOnClickListener {
            Log.i("check date click", "Click on set date")

            val dateInMillis = Calendar.getInstance().timeInMillis

            val constraints = CalendarConstraints.Builder().setOpenAt(dateInMillis).build()

            val pickerDate =
                MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraints).build()

            pickerDate.addOnPositiveButtonClickListener {

                temporaryStart = Date(it)
                temporaryEnd = Date(it)

                binding.meetingSetDateTextview1.text = sdf.format(temporaryStart)
                binding.meetingSetDateTextview2.text = sdf.format(temporaryEnd)

                binding.meetingSetTimeBegin.text = stf.format(temporaryStart)
                binding.meetingSetTimeEnd.text = stf.format(temporaryEnd)

            }

            pickerDate.show(this.parentFragmentManager, "select date")
        }

        binding.meetingSetTimeBegin.setOnClickListener {
            Log.i("check time start click", "Click on set meeting begin")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                temporaryStart.hours = pickerTime.hour
                temporaryStart.minutes = pickerTime.minute

                temporaryEnd.hours = pickerTime.hour + 1
                temporaryEnd.minutes = pickerTime.minute

                if (temporaryEnd.hours == 0)
                    binding.meetingSetDateTextview2.text = sdf.format(temporaryEnd)

                binding.meetingSetTimeBegin.text = stf.format(temporaryStart)
                binding.meetingSetTimeEnd.text = stf.format(temporaryEnd)
            }

            pickerTime.show(this.parentFragmentManager, "set time begin")
        }

        binding.meetingSetTimeEnd.setOnClickListener {
            Log.i("check time end click", "Click on set meeting end")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                temporaryEnd.hours = pickerTime.hour
                temporaryEnd.minutes = pickerTime.minute

                binding.meetingSetTimeEnd.text = stf.format(temporaryEnd)
            }

            pickerTime.show(this.parentFragmentManager, "set time begin")
        }

        binding.meetingSaveButton.setOnClickListener {
            Log.i("check save click", "Click on save meeting")

            val meetingTitle = binding.meetingTitleEdittext.text.toString()
            val meetingNote = binding.meetingNoteEdittext.text.toString()

            val meetingRec = MeetingDataModel(
                0, meetingTitle, meetingNote, DatePeriodModel(
                    temporaryStart.time, temporaryEnd.time
                )
            )

            DataBaseHelper(context).addMeeting(meetingRec)

            requireActivity().onBackPressed()
        }

    }

    override fun onPause() {
        super.onPause()
        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
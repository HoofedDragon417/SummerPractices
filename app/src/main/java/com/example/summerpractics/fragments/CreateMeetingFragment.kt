package com.example.summerpractics.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.database.DataBaseHelper
import com.example.summerpractics.databinding.FragmentCreateMeetingBinding
import com.example.summerpractics.models.MeetingDataModel
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

        private val sdf = SimpleDateFormat("dd/MM/yyyy")
        private val stf = SimpleDateFormat("HH:mm")

        var startMeeting = Date()
        var endMeeting = Date()

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



        binding.startMeetingTextview.setOnClickListener {

            Log.i("check date click", "Click on set date")

            val dateInMillis = startMeeting.time

            val constraints = CalendarConstraints.Builder().setOpenAt(dateInMillis).build()

            val pickerDate =
                MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraints).build()

            pickerDate.addOnPositiveButtonClickListener {

                startMeeting = Date(it)
                endMeeting = Date(it)

                binding.meetingSetDateTextview1.text = sdf.format(startMeeting)
                binding.meetingSetDateTextview2.text = sdf.format(endMeeting)

                binding.meetingSetTimeBegin.text = stf.format(startMeeting)
                binding.meetingSetTimeEnd.text = stf.format(endMeeting)

            }

            pickerDate.show(this.parentFragmentManager, "select date")

        }

        binding.meetingSetTimeBegin.setOnClickListener {

            Log.i("check time start click", "Click on set meeting begin")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                startMeeting.hours = pickerTime.hour
                endMeeting.hours = pickerTime.hour + 1

                startMeeting.minutes = pickerTime.minute
                endMeeting.minutes = pickerTime.minute

                binding.meetingSetTimeBegin.text = stf.format(startMeeting)
                binding.meetingSetTimeEnd.text = stf.format(endMeeting)

            }

            pickerTime.show(this.parentFragmentManager, "set time begin")

        }

        binding.meetingSetTimeEnd.setOnClickListener {

            Log.i("check time end click", "Click on set meeting end")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                endMeeting.hours = pickerTime.hour

                endMeeting.minutes = pickerTime.minute

                binding.meetingSetTimeEnd.text = stf.format(endMeeting)

            }

            pickerTime.show(this.parentFragmentManager, "set time begin")

        }

        binding.meetingSaveButton.setOnClickListener {

            Log.i("check save click", "Click on save meeting")

            val meetingTitle = binding.meetingTitleEdittext.text.toString()
            val meetingNote = binding.meetingNoteEdittext.text.toString()
            val start = startMeeting.time
            val end = endMeeting.time

            val meetingRec = MeetingDataModel(0, meetingTitle, meetingNote, start, end)

            DataBaseHelper(context).addMeeting(meetingRec)

            requireActivity().onBackPressed()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
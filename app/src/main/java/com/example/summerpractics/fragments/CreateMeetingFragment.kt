package com.example.summerpractics.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        private val sdf = SimpleDateFormat("dd/MM/yyyy")
        private val stf = SimpleDateFormat("HH:mm")

        var calendar = Calendar.getInstance()
        var temporaryStart: Long = 0
        var temporaryEnd: Long = 0

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

            val dateInMillis = calendar.timeInMillis

            val constraints = CalendarConstraints.Builder().setOpenAt(dateInMillis).build()

            val pickerDate =
                MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraints).build()

            pickerDate.addOnPositiveButtonClickListener {

                binding.meetingSetDateTextview1.text = sdf.format(calendar)
                binding.meetingSetDateTextview2.text = sdf.format(calendar)

                binding.meetingSetTimeBegin.text = stf.format(calendar)
                binding.meetingSetTimeEnd.text = stf.format(calendar)

            }

            pickerDate.show(this.parentFragmentManager, "select date")
        }

        binding.meetingSetTimeBegin.setOnClickListener {
            Log.i("check time start click", "Click on set meeting begin")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                calendar[Calendar.HOUR] = pickerTime.hour
                calendar[Calendar.MINUTE] = pickerTime.minute

                binding.meetingSetTimeBegin.text = stf.format(calendar)

                temporaryStart = calendar.timeInMillis
                calendar[Calendar.HOUR]++

                binding.meetingSetTimeEnd.text = stf.format(calendar)

            }

            pickerTime.show(this.parentFragmentManager, "set time begin")
        }

        binding.meetingSetTimeEnd.setOnClickListener {
            Log.i("check time end click", "Click on set meeting end")

            val pickerTime = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()

            pickerTime.addOnPositiveButtonClickListener {

                calendar[Calendar.HOUR] = pickerTime.hour
                calendar[Calendar.MINUTE] = pickerTime.minute

                temporaryEnd = calendar.timeInMillis

                binding.meetingSetTimeEnd.text = stf.format(calendar)

            }

            pickerTime.show(this.parentFragmentManager, "set time begin")
        }

        binding.meetingSaveButton.setOnClickListener {
            Log.i("check save click", "Click on save meeting")

            val meetingTitle = binding.meetingTitleEdittext.text.toString()
            val meetingNote = binding.meetingNoteEdittext.text.toString()

            val meetingRec = MeetingDataModel(
                0, meetingTitle, meetingNote, DatePeriodModel(
                    temporaryStart, temporaryEnd
                )
            )

            DataBaseHelper(context).addMeeting(meetingRec)

            requireActivity().onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        
    }

}
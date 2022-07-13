package com.example.summerpractics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.adapters.MeetingAdapter
import com.example.summerpractics.databinding.FragmentMeetingBinding
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.storage.SharedPreference

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

        val dates = SharedPreference(requireContext()).getDate()

        val listOfMeetings = DataBaseHelper(context).viewMeetings(dates)

        binding.recyclerMeetingFragment.adapter =
            MeetingAdapter(requireContext(), listOfMeetings)
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
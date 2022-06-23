package com.example.summerpractics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.adapters.ViewPagerAdapter
import com.example.summerpractics.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fragments = arrayListOf(

            MeetingFragment(),
            TaskFragment()

        )

        val adapter = ViewPagerAdapter(fragments, requireActivity())
        binding.pager.adapter = adapter

        binding.createTaskFab.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.test_fragment, CreateTaskFragment.newInstance())
                .addToBackStack(null).commit()

        }

        binding.createMeetingFab.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.test_fragment, CreateMeetingFragment.newInstance())
                .addToBackStack(null).commit()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {

        fun newInstance() =
            MainFragment()
    }
}
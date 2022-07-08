package com.example.summerpractics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.adapters.ViewPagerAdapter
import com.example.summerpractics.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        create()

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

        binding.testNavBar.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.meetings -> binding.pager.currentItem = 0

                R.id.tasks -> binding.pager.currentItem = 1

            }

            true

        }

    }

    override fun onResume() {
        super.onResume()

        create()

    }

    private fun create() {

        val fragments = arrayListOf(

            MeetingFragment(),
            TaskFragment()

        )

        val tabsTitle = arrayOf(getString(R.string.meetings), getString(R.string.tasks))

        val adapter = ViewPagerAdapter(fragments, requireActivity())
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->

            tab.text = tabsTitle[position]

        }.attach()

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
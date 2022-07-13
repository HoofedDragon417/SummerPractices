package com.example.summerpractics.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.adapters.ViewPagerAdapter
import com.example.summerpractics.databinding.FragmentMainBinding
import com.example.summerpractics.storage.SharedPreference
import com.google.android.material.tabs.TabLayoutMediator

const val THEME_ID = "theme_id"
const val ITEM_ID = "item_id"

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

        val themeId = SharedPreference(requireContext()).getInt(THEME_ID)

        if (themeId != 0) {
            AppCompatDelegate.setDefaultNightMode(themeId)
        }

        binding.testNavBar.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.meetings -> parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CreateMeetingFragment.newInstance())
                    .addToBackStack(null).commit()

                R.id.tasks -> parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CreateTaskFragment.newInstance())
                    .addToBackStack(null).commit()

                R.id.theme -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.change_theme))
                    val styles = arrayOf("System", "Light", "Dark")
                    val checkedItem = SharedPreference(requireContext()).getInt(ITEM_ID)

                    builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
                        when (which) {
                            0 -> {
                                saveTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, 0)
                                dialog.dismiss()
                            }
                            1 -> {
                                saveTheme(AppCompatDelegate.MODE_NIGHT_NO, 1)
                                dialog.dismiss()
                            }
                            2 -> {
                                saveTheme(AppCompatDelegate.MODE_NIGHT_YES, 2)
                                dialog.dismiss()
                            }
                        }
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
            true
        }
    }

    private fun saveTheme(theme: Int, item: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
        SharedPreference(requireContext()).saveInt(THEME_ID, theme)
        SharedPreference(requireContext()).saveInt(ITEM_ID, item)
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
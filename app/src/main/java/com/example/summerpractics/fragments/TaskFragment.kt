package com.example.summerpractics.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.summerpractics.R
import com.example.summerpractics.adapters.TaskAdapter
import com.example.summerpractics.databinding.FragmentTaskBinding
import com.example.summerpractics.storage.DataBaseHelper
import com.example.summerpractics.storage.SharedPreference

private const val ITEM_PARAM_ID = "param_id"
private const val PARAM = "param"

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (SharedPreference(requireContext()).getInt(THEME_ID)) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.taskImageviewToolbar.setImageResource(R.drawable.ic_baseline_note_white_24)
            AppCompatDelegate.MODE_NIGHT_NO -> binding.taskImageviewToolbar.setImageResource(R.drawable.ic_baseline_note_black_24)
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM ->
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> binding.taskImageviewToolbar.setImageResource(
                        R.drawable.ic_baseline_note_white_24
                    )
                    Configuration.UI_MODE_NIGHT_NO -> binding.taskImageviewToolbar.setImageResource(
                        R.drawable.ic_baseline_note_black_24
                    )
                }
        }

        showListOfTasksWithParam(0, 0)

        binding.taskImageviewToolbar.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.choose_tasks))
            val paramArray = arrayOf("Unfinished", "Finished", "All")
            val checkedItem = SharedPreference(requireContext()).getInt(ITEM_PARAM_ID)

            builder.setSingleChoiceItems(paramArray, checkedItem) { dialog, which ->
                when (which) {
                    0 -> {
                        showListOfTasksWithParam(0, 0)
                        dialog.dismiss()
                    }
                    1 -> {
                        showListOfTasksWithParam(1, 1)
                        SharedPreference(requireContext()).saveInt(ITEM_PARAM_ID, 1)
                        dialog.dismiss()
                    }
                    2 -> {
                        showAllTasks(2)
                        dialog.dismiss()
                    }
                }
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun showListOfTasksWithParam(param: Int, id: Int) {
        val listOfTasks = DataBaseHelper(context).viewTasksWithParameter(param)

        SharedPreference(requireContext()).saveInt(PARAM, param)
        SharedPreference(requireContext()).saveInt(ITEM_PARAM_ID, id)

        binding.recyclerTaskFragment.adapter = TaskAdapter(requireContext(), listOfTasks)
    }

    private fun showAllTasks(id: Int) {
        val listOfTasks = DataBaseHelper(context).viewAllTasks()
        binding.recyclerTaskFragment.adapter = TaskAdapter(requireContext(), listOfTasks)
        SharedPreference(requireContext()).saveInt(ITEM_PARAM_ID, id)
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
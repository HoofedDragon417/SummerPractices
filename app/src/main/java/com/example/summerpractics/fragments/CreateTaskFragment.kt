package com.example.summerpractics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.databinding.FragmentCreateTaskBinding
import com.example.summerpractics.models.TaskDataModel
import com.example.summerpractics.storage.DataBaseHelper
import java.util.*

class CreateTaskFragment : Fragment() {

    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = requireNotNull(_binding)

    companion object {

        fun newInstance() =
            CreateTaskFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskSaveButton.setOnClickListener {
            val taskTitle = binding.taskTitleEdittext.text.toString()
            val taskNote = binding.taskNoteEdittext.text.toString()
            val taskDuration = binding.taskDurationEdittext.text.toString().toDouble()
            val taskPriority = binding.taskPriorityEdittext.text.toString().toInt()
            val timeOfCreation = Calendar.getInstance().timeInMillis

            val taskRec =
                TaskDataModel(0, taskTitle, taskNote, taskDuration, taskPriority, 0, timeOfCreation)

            DataBaseHelper(context).addTask(taskRec)

            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
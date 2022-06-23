package com.example.summerpractics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.summerpractics.adapters.TaskAdapter
import com.example.summerpractics.database.DataBaseHelper
import com.example.summerpractics.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfTasks = DataBaseHelper(context).viewTasks()

        binding.recyclerTaskFragment.adapter = TaskAdapter(requireContext(), listOfTasks)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
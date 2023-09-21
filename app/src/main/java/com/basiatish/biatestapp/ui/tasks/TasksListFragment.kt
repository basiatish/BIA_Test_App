package com.basiatish.biatestapp.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.databinding.TasksListFragmentBinding
import com.basiatish.biatestapp.ui.tasks.interfaces.OnButtonClickListener
import javax.inject.Inject

class TasksListFragment : Fragment(), OnButtonClickListener {

    private var _binding: TasksListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskListAdapter

    @Inject lateinit var viewModel: TasksListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.tasksComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TasksListFragmentBinding.inflate(inflater, container, false)
        viewModel.getTaskList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskListAdapter(this)
        binding.tasksList.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        binding.tasksList.layoutManager = layoutManager
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.taskList.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.error.observe(this.viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewDetailsClick(id: Int) {
        val action = TasksListFragmentDirections.actionTasksFragmentToTaskDetailsFragment(id)
        findNavController().navigate(action)
    }

}
package com.basiatish.biatestapp.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.databinding.TasksListFragmentBinding
import javax.inject.Inject

class TasksListFragment : Fragment() {

    private var _binding: TasksListFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: TasksListViewModel

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
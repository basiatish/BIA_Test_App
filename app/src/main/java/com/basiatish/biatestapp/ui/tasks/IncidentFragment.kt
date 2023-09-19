package com.basiatish.biatestapp.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.databinding.IncidentFragmentBinding
import com.basiatish.domain.entities.Incident
import javax.inject.Inject

class IncidentFragment : Fragment() {

    private var _binding: IncidentFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: IncidentFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: TaskDetailsViewModel

    private lateinit var incident: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.tasksComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IncidentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val incidentText: String? = navArgs.incidentText
        if (incidentText != null) bind(incidentText)
        setupListeners()
    }

    private fun setupListeners() {
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            binding.saveButton.isEnabled = true
            when (id) {
                binding.addressProblemButton.id -> incident = binding.addressProblemButton.text.toString()
                binding.wayProblemButton.id -> incident = binding.wayProblemButton.text.toString()
                binding.loadProblemButton.id -> incident = binding.loadProblemButton.text.toString()
            }
        }
        binding.saveButton.setOnClickListener {
            viewModel.saveIncident(navArgs.taskID, incident)
            NavigationUI.navigateUp(findNavController(), null)
        }
    }

    private fun bind(text: String) {
        when (text) {
            binding.addressProblemButton.text -> binding.addressProblemButton.isChecked = true
            binding.wayProblemButton.text -> binding.wayProblemButton.isChecked = true
            binding.loadProblemButton.text -> binding.loadProblemButton.isChecked = true
        }
    }

}
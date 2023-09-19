package com.basiatish.biatestapp.ui.tasks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.DocumentsFragmentBinding
import javax.inject.Inject

class TaskDocumentsFragment : Fragment(), OnDeleteButtonClickListener {

    private var _binding: DocumentsFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: TaskDocumentsFragmentArgs by navArgs()

    private lateinit var adapter: DocumentsListAdapter

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val fileName = (requireActivity().application as App)
                .getFileNameFromUri(requireContext(), data?.data!!)
            viewModel.addDocument(fileName, data.data)
        }
    }

    @Inject
    lateinit var viewModel: TaskDetailsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.tasksComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DocumentsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DocumentsListAdapter(this)
        binding.documentsList.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        binding.documentsList.layoutManager = layoutManager
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.submitButton.setOnClickListener {
            viewModel.upload(this.viewLifecycleOwner, navArgs.contactName, navArgs.taskID)
            binding.submitButton.isEnabled = false
        }
        binding.addPhotoButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            resultLauncher.launch(intent)
        }
    }

    private fun setupObservers() {
        viewModel.documents.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
            binding.submitButton.isEnabled = it.isNotEmpty()
        }
        viewModel.status.observe(this.viewLifecycleOwner) {
            binding.submitButton.isEnabled = true
            Toast.makeText(requireContext(),
                resources.getText(R.string.documents_toast), Toast.LENGTH_SHORT).show()
            viewModel.updateTaskStatus(navArgs.taskID, "Done")
            findNavController().navigateUp()
        }
    }

    override fun onDeleteClick(name: String) {
        viewModel.removeDocument(name)
    }
}
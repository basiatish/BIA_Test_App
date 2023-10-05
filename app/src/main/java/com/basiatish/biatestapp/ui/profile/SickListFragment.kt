package com.basiatish.biatestapp.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.SickListFragmentBinding
import com.basiatish.biatestapp.ui.profile.interfaces.OnButtonClick
import com.basiatish.domain.entities.SickListItem
import javax.inject.Inject

class SickListFragment : Fragment(), OnButtonClick {

    private var _binding: SickListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var addButton: AppCompatImageButton
    private lateinit var adapter: SickListAdapter

    @Inject
    lateinit var viewModel: SickListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.profileComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SickListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSickList()
        addButton = requireActivity().findViewById(R.id.add_sick_list)
        addButton.isEnabled = false
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        addButton.setOnClickListener {
            val action = SickListFragmentDirections.actionSickListFragmentToAddSickFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        viewModel.sickList.observe(this.viewLifecycleOwner) {
            if (it != null) {
                adapter = SickListAdapter(it, this)
                val layoutManager = LinearLayoutManager(requireContext())
                binding.sickList.adapter = adapter
                binding.sickList.layoutManager = layoutManager
                if (it.isEmpty()) {
                    binding.freeListText.visibility = View.VISIBLE
                    addButton.isEnabled = true
                } else {
                    binding.freeListText.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(sick: SickListItem) {
        viewModel.closeList(sick)
    }

}
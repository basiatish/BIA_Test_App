package com.basiatish.biatestapp.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.AddSickFragmentBinding
import com.basiatish.biatestapp.ui.profile.interfaces.OnRangeListener
import com.basiatish.domain.entities.SickDay
import javax.inject.Inject

class AddSickFragment : Fragment(), OnRangeListener {

    private var _binding: AddSickFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SickCalendarAdapter

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
        _binding = AddSickFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCalendar()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.calendarView.nextButton.setOnClickListener {
            changeDate(true)
            viewModel.cleanRange()
            binding.chooseButton.text = resources.getText(R.string.choose)
        }
        binding.calendarView.prevButton.setOnClickListener {
            changeDate(false)
            viewModel.cleanRange()
            binding.chooseButton.text = resources.getText(R.string.choose)
        }
        binding.cleanButton.setOnClickListener {
            viewModel.getCalendar()
            viewModel.cleanRange()
            binding.chooseButton.text = resources.getText(R.string.choose)
        }
        binding.chooseButton.setOnClickListener {
            it.isEnabled = false
            viewModel.saveRange()
        }
    }

    private fun setupObservers() {
        viewModel.calendar.observe(this.viewLifecycleOwner) {
            adapter = SickCalendarAdapter(it, this)
            val layoutManager: RecyclerView.LayoutManager =
                GridLayoutManager(requireContext(), 7)
            binding.calendarView.calendarRecyclerView.layoutManager = layoutManager
            binding.calendarView.calendarRecyclerView.adapter = adapter
            val divider = DividerItemDecoration(binding.calendarView.calendarRecyclerView.context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.grid_divider, requireContext().theme)!!)
            binding.calendarView.calendarRecyclerView.addItemDecoration(divider)
            binding.calendarView.monthYear.text =
                resources.getString(
                    R.string.month_year,
                    viewModel.getMonthFromDate(),
                    viewModel.getYearFromDate())
        }
        viewModel.range.observe(this.viewLifecycleOwner) {
            if (it.size > 1) {
                adapter.notifyItemRangeChanged(it[1], it.size - 2)
                for (i in 0 until adapter.itemCount) {
                    binding.calendarView.calendarRecyclerView.getChildAt(i).isClickable = false
                }
                setButtonText(false)
                binding.cleanButton.isEnabled = true
            } else if (it.size == 1) {
                setButtonText(true)
            } else {
                binding.chooseButton.isEnabled = false
                binding.cleanButton.isEnabled = false
            }
        }
        viewModel.cleanPosition.observe(this.viewLifecycleOwner) {
            binding.calendarView.calendarRecyclerView.getChildAt(it).isSelected = false
        }
        viewModel.saveStatus.observe(this.viewLifecycleOwner) {
            if (it == "Success") {
                findNavController().navigateUp()
            } else {
                binding.chooseButton.isEnabled = true
            }
        }
    }

    private fun setButtonText(isSingleDay: Boolean) {
        val range = viewModel.getDaysRangeText(isSingleDay)
        binding.chooseButton.text = resources.getString(R.string.choose_range, range)
        binding.chooseButton.isEnabled = true
    }

    private fun changeDate(isNext: Boolean) {
        if (isNext) {
            viewModel.increaseDate()
            viewModel.getCalendar()
        } else {
            viewModel.decreaseDate()
            viewModel.getCalendar()
        }
    }

    override fun onDayClick(day: SickDay, position: Int) {
        if (day.isCurrentMonth) {
            viewModel.createRange(day.value, position)
        } else if (day.value.toInt() <= 31 && position < 7) {
            changeDate(false)
        } else {
            changeDate(true)
        }

    }

}
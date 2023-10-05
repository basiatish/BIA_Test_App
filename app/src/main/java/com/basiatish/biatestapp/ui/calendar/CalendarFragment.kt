package com.basiatish.biatestapp.ui.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.CalendarFragmentBinding
import com.basiatish.biatestapp.ui.calendar.interfaces.OnDayListener
import com.basiatish.domain.entities.CalendarDay
import javax.inject.Inject


class CalendarFragment : Fragment(), OnDayListener {

    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cellText: String

    @Inject
    lateinit var viewModel: CalendarViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.calendarComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CalendarFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCalendar()
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.calendar.observe(this.viewLifecycleOwner) {
            if (it != null) {
                val calendarAdapter = CalendarAdapter(it, this)
                val layoutManager: RecyclerView.LayoutManager =
                    GridLayoutManager(requireContext(), 7)
                binding.calendarView.calendarRecyclerView.layoutManager = layoutManager
                binding.calendarView.calendarRecyclerView.adapter = calendarAdapter
                val divider = DividerItemDecoration(binding.calendarView.calendarRecyclerView.context, DividerItemDecoration.VERTICAL)
                divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.grid_divider, requireContext().theme)!!)
                binding.calendarView.calendarRecyclerView.addItemDecoration(divider)
                binding.calendarView.monthYear.text =
                    resources.getString(
                        R.string.month_year,
                        viewModel.getMonthFromDate(),
                        viewModel.getYearFromDate())
            }
        }
        viewModel.status.observe(this.viewLifecycleOwner) {
            if (it == "Success") {
                viewModel.getCalendar()
            }
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            binding.pickContainer.visibility = View.GONE
            val isWorkDay = binding.textPicker.text.toString() == resources.getString(R.string.work_day)
            viewModel.saveDay(cellText, isWorkDay)
        }
        binding.declineButton.setOnClickListener {
            binding.pickContainer.visibility = View.GONE
        }
        binding.calendarView.nextButton.setOnClickListener {
            changeDate(true)
        }
        binding.calendarView.prevButton.setOnClickListener {
            changeDate(false)
        }
    }

    private fun changeDate(isNext: Boolean) {
        binding.pickContainer.visibility = View.GONE
        if (isNext) {
            viewModel.increaseDate()
            viewModel.getCalendar()
        } else {
            viewModel.decreaseDate()
            viewModel.getCalendar()
        }
    }

    override fun onResume() {
        super.onResume()
        setupTextView()
    }

    private fun setupTextView() {
        val textVariants = listOf(resources.getText(R.string.work_day), resources.getText(R.string.rest_day))
        val adapter = ArrayAdapter(requireContext(), R.layout.text_picker_item, textVariants)
        binding.textPicker.setAdapter(adapter)
        binding.textPicker.setDropDownBackgroundResource(R.color.white)
    }

    override fun onDayClick(day: CalendarDay, position: Int) {
        cellText = day.value
        if (day.isCurrentMonth) {
            binding.date.text = resources.getString(
                R.string.selected_date,
                cellText,
                viewModel.getMonthFromDate(),
                viewModel.getYearFromDate())
            binding.pickContainer.visibility = View.VISIBLE
        } else if (cellText.toInt() <= 31 && position < 7) {
            changeDate(false)
        } else {
            changeDate(true)
        }
    }
}
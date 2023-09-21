package com.basiatish.biatestapp.ui.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.CalendarFragmentBinding
import com.basiatish.biatestapp.ui.calendar.interfaces.OnDayListener
import java.time.LocalDate
import javax.inject.Inject


class CalendarFragment : Fragment(), OnDayListener {

    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cellText: String

    private lateinit var selectedDate: LocalDate

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
        selectedDate = LocalDate.now()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarView.nextButton.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            viewModel.getCalendar(selectedDate)
            binding.pickContainer.visibility = View.GONE
        }
        binding.calendarView.prevButton.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            viewModel.getCalendar(selectedDate)
            binding.pickContainer.visibility = View.GONE
        }
        viewModel.getCalendar(selectedDate)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.calendar.observe(this.viewLifecycleOwner) {
            if (it != null) {
                println()
                val calendarAdapter = CalendarAdapter(it, this)
                val layoutManager: RecyclerView.LayoutManager =
                    GridLayoutManager(requireContext(), 7)
                binding.calendarView.calendarRecyclerView.layoutManager = layoutManager
                binding.calendarView.calendarRecyclerView.adapter = calendarAdapter
                binding.calendarView.monthYear.text =
                    resources.getString(
                        R.string.month_year,
                        viewModel.getMonthFromDate(selectedDate),
                        viewModel.getYearFromDate(selectedDate))
            }
        }

        viewModel.status.observe(this.viewLifecycleOwner) {
            if (it == "Success") {
                viewModel.getCalendar(selectedDate)
            }
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            binding.pickContainer.visibility = View.GONE
            val isWorkDay = binding.textPicker.text.toString() == resources.getString(R.string.work_day)
            viewModel.saveDay(selectedDate, cellText, isWorkDay)
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

    override fun onDayClick(position: Int, text: String) {
        cellText = text
        binding.date.text = resources.getString(
            R.string.selected_date,
            text,
            viewModel.getMonthFromDate(selectedDate),
            viewModel.getYearFromDate(selectedDate))
        binding.pickContainer.visibility = View.VISIBLE
    }

}
package com.basiatish.biatestapp.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.CalendarCellBinding
import com.basiatish.biatestapp.ui.calendar.interfaces.OnDayListener
import com.basiatish.domain.entities.CalendarDay

class CalendarAdapter(
    private val daysOfMonth: List<CalendarDay>,
    private val onDayListener: OnDayListener
) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selected: View? = null

    inner class CalendarViewHolder(private val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, context: Context) {
            binding.calendarDay.text = daysOfMonth[position].value
            if (daysOfMonth[position].isWeekend) {
                binding.calendarDay.setTextColor(context.resources.getColorStateList(R.color.calendar_rest_cell_text_color, context.theme))
            }
            if (!daysOfMonth[position].isWorkDay && !daysOfMonth[position].isToday) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.calendar_rest_date_container, context.theme)
            } else if (daysOfMonth[position].isWorkDay && !daysOfMonth[position].isToday) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.calendar_work_day_container, context.theme)
            } else if (daysOfMonth[position].isToday && daysOfMonth[position].isWorkDay) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.today_calendar_date_container_work, context.theme)
            } else if (daysOfMonth[position].isToday && !daysOfMonth[position].isWorkDay) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.today_calendar_date_container_vacation, context.theme)
            }
            if (!daysOfMonth[position].isCurrentMonth && daysOfMonth[position].isWorkDay) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.calendar_prev_next_day_container, context.theme)
                binding.calendarDay.setTextColor(context.resources.getColor(R.color.middle_gray_blue, context.theme))
            } else if (!daysOfMonth[position].isCurrentMonth && (daysOfMonth[position].isWeekend || !daysOfMonth[position].isWorkDay)) {
                binding.calendarDay.setTextColor(context.resources.getColor(R.color.middle_gray_blue, context.theme))
            }

            binding.cell.setOnClickListener {
                if (daysOfMonth[position].isCurrentMonth) {
                    onDayListener.onDayClick(daysOfMonth[position], position)
                    selected?.isSelected = false
                    binding.cell.isSelected = true
                    selected = binding.cell
                } else {
                    onDayListener.onDayClick(daysOfMonth[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        val layoutParams = view.layoutParams
        layoutParams.height = parent.width / 7
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(position, holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }
}
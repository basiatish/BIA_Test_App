package com.basiatish.biatestapp.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.CalendarCellBinding
import com.basiatish.biatestapp.ui.profile.interfaces.OnRangeListener
import com.basiatish.domain.entities.SickDay

class SickCalendarAdapter(
    private val daysOfMonth: List<SickDay>,
    private val onRangeListener: OnRangeListener
) : RecyclerView.Adapter<SickCalendarAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, context: Context) {
            binding.calendarDay.text = daysOfMonth[position].value
            val params = binding.cell.layoutParams as GridLayoutManager.LayoutParams
            params.setMargins(0, 2, 0, 2)
            binding.cell.layoutParams = params
            if (daysOfMonth[position].isWeekend) {
                binding.calendarDay.setTextColor(context.resources.getColorStateList(R.color.calendar_rest_cell_text_color, context.theme))
            }
            if (daysOfMonth[position].isToday) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.today_calendar_date_container_vacation, context.theme)
            }
            if (!daysOfMonth[position].isCurrentMonth) {
                binding.calendarDay.setTextColor(context.resources.getColor(R.color.middle_gray_blue, context.theme))
            }
            if (daysOfMonth[position].isSelected) {
                binding.cell.background = ResourcesCompat.getDrawable(context.resources, R.drawable.sick_calendar_range, context.theme)
            }
            binding.cell.setOnClickListener {
                if (daysOfMonth[position].isCurrentMonth) {
                    onRangeListener.onDayClick(daysOfMonth[position], position)
                    binding.cell.isSelected = true
                } else {
                    onRangeListener.onDayClick(daysOfMonth[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        val layoutParams = view.layoutParams
        layoutParams.height = parent.width / 7
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }
}
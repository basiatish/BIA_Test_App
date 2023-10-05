package com.basiatish.biatestapp.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basiatish.biatestapp.R
import com.basiatish.biatestapp.databinding.SickListItemBinding
import com.basiatish.biatestapp.ui.profile.interfaces.OnButtonClick
import com.basiatish.domain.entities.SickListItem

class SickListAdapter(
    private val sickList: List<SickListItem>,
    private val onButtonClick: OnButtonClick
) : RecyclerView.Adapter<SickListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: SickListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, context: Context) {
            binding.rangeText.text = context.resources
                .getString(R.string.sick_list_item_range,
                    sickList[position].startDate, sickList[position].endDate)
            binding.closeListButton.setOnClickListener {
                it.isEnabled = false
                onButtonClick.onClick(sickList[position])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SickListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return sickList.size
    }

}
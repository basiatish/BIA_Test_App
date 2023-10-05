package com.basiatish.biatestapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.SickDay
import com.basiatish.domain.entities.SickListItem
import com.basiatish.domain.usecases.CloseSickListUseCase
import com.basiatish.domain.usecases.GetSickCalendarUseCase
import com.basiatish.domain.usecases.GetSickListUseCase
import com.basiatish.domain.usecases.SaveRangeUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@FragmentScope
class SickListViewModel @Inject constructor(
    private val getSickCalendarUseCase: GetSickCalendarUseCase,
    private val saveRangeUseCase: SaveRangeUseCase,
    private val getSickListUseCase: GetSickListUseCase,
    private val closeSickListUseCase: CloseSickListUseCase
) : ViewModel() {

    private var selectedDate = LocalDate.now()

    private var _range = MutableLiveData<List<Int>>()
    val range: LiveData<List<Int>> = _range

    private val rangeList = mutableListOf<Int>()
    private val rangeDays = mutableListOf<String>()

    private val _calendar = MutableLiveData<List<SickDay>>()
    val calendar: LiveData<List<SickDay>> = _calendar

    private val _cleanPosition = MutableLiveData<Int>()
    val cleanPosition: LiveData<Int> = _cleanPosition

    private val _sickList = MutableLiveData<List<SickListItem>?>()
    val sickList: LiveData<List<SickListItem>?> = _sickList

    private val _saveStatus = MutableLiveData<String>()
    val saveStatus: LiveData<String> = _saveStatus

    fun getSickList() {
        viewModelScope.launch {
            when (val response = getSickListUseCase.invoke()) {
                is Result.Success -> {
                    _sickList.value = response.resultData
                }
                is Result.Error -> {
                    Log.e("sickList", response.exception.toString())
                }
            }
        }
    }

    fun closeList(sick: SickListItem) {
        viewModelScope.launch {
            when (val response = closeSickListUseCase.invoke(sick.startDate, sick.endDate)) {
                is Result.Success -> {
                    _sickList.value = listOf()
                }
                is Result.Error -> {
                    Log.e("sickList", response.exception.toString())
                }
            }
        }
    }

    fun getCalendar() {
        viewModelScope.launch {
            _calendar.value = getSickCalendarUseCase.invoke(selectedDate)
        }
    }

    fun saveRange() {
        viewModelScope.launch {
            val start = LocalDate.of(getYearFromDate().toInt(), getMonthFromDateInt(), rangeDays[0].toInt())
            val startMills = start.atStartOfDay().toInstant(ZoneId.systemDefault().rules.getOffset(start.atStartOfDay())).toEpochMilli()
            val end = LocalDate.of(getYearFromDate().toInt(), getMonthFromDateInt(), rangeDays[0].toInt() + rangeList.size - 1)
            val endMills = end.atStartOfDay().toInstant(ZoneId.systemDefault().rules.getOffset(end.atStartOfDay())).toEpochMilli()
            when (val response = saveRangeUseCase.invoke(startMills, endMills)) {
                is Result.Success -> {
                    _saveStatus.value = "Success"
                    getSickList()
                }
                is Result.Error -> {
                    _saveStatus.value = "Error"
                    Log.e("sickList", response.exception.toString())
                }
            }
        }
    }

    fun createRange(day: String, position: Int) {
        if (rangeList.isEmpty()) {
            rangeList.add(position)
            rangeDays.add(day)
            _calendar.value?.get(position)?.isSelected = true
            _range.value = rangeList
        }
        else if (rangeList.size == 1 && rangeList[0] > position) {
            _calendar.value?.get(rangeList[0])?.isSelected = false
            _cleanPosition.value = rangeList[0]
            rangeList.clear()
            rangeList.add(position)
            rangeDays.clear()
            rangeDays.add(day)
            _calendar.value?.get(position)?.isSelected = true
            _range.value = rangeList
        } else if (rangeList.size == 1 && rangeList[0] < position) {
            for (i in rangeList[0] + 1..position) {
                rangeList.add(i)
                _calendar.value?.get(i)?.isSelected = true
                _range.value = rangeList
            }
        }
    }

    fun getDaysRangeText(isSingleDay: Boolean): String {
        var result = ""
        val startDay = selectedDate.withDayOfMonth(rangeDays[0].toInt()).dayOfMonth
        if (isSingleDay) {
            result = "$startDay ${getMonthFromDate()}"
            return result
        }
        val endDay = selectedDate.withDayOfMonth(rangeDays[0].toInt() + rangeList.size - 1).dayOfMonth
        result = "$startDay - $endDay ${getMonthFromDate()}"
        return result
    }

    fun cleanRange() {
        rangeList.clear()
        rangeDays.clear()
        _range.value = rangeList
    }

    fun increaseDate() {
        selectedDate = selectedDate.plusMonths(1)
    }

    fun decreaseDate() {
        selectedDate = selectedDate.minusMonths(1)
    }

    fun getMonthFromDate(): String {
        return selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun getMonthFromDateInt(): Month {
        return selectedDate.month
    }

    fun getYearFromDate(): String {
        return selectedDate.year.toString()
    }

}
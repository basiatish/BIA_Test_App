package com.basiatish.biatestapp.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.DayEntity
import com.basiatish.domain.usecases.GetCalendarUseCase
import com.basiatish.domain.usecases.SaveDayUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@FragmentScope
class CalendarViewModel @Inject constructor(
    private val getCalendarUseCase: GetCalendarUseCase,
    private val saveDayUseCase: SaveDayUseCase
): ViewModel() {

    private val _calendar = MutableLiveData<List<DayEntity>?>()
    val calendar: LiveData<List<DayEntity>?> = _calendar

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> = _status

    fun getCalendar(date: LocalDate) {
        viewModelScope.launch {
            when (val result = getCalendarUseCase.invoke(date)) {
                is Result.Success -> {
                    _calendar.value = result.resultData
                }
                is Result.Error -> {
                    Log.e("Calendar", result.exception.message.toString())
                }
            }
        }
    }

    fun saveDay(date: LocalDate, text: String, isWorkDay: Boolean) {
        viewModelScope.launch {
            val time = LocalDate.of(getYearFromDate(date).toInt(), getMonthFromDateInt(date), text.toInt())
            val timeMills = time.atStartOfDay().toInstant(ZoneId.systemDefault().rules.getOffset(time.atStartOfDay())).toEpochMilli()
            when (val upload = saveDayUseCase.invoke(timeMills, isWorkDay)) {
                is Result.Success -> {
                    _status.value = upload.resultData
                }
                is Result.Error -> {
                    Log.e("Calendar", upload.exception.message.toString())
                }
            }
        }
    }

    fun getMonthFromDate(date: LocalDate): String {
        return date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    private fun getMonthFromDateInt(date: LocalDate): Month {
        return date.month
    }

    fun getYearFromDate(date: LocalDate): String {
        return date.year.toString()
    }
}
package com.basiatish.biatestapp.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.CalendarDay
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

    private val _calendar = MutableLiveData<List<CalendarDay>?>()
    val calendar: LiveData<List<CalendarDay>?> = _calendar

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> = _status

    private var selectedDate = LocalDate.now()

    fun getCalendar() {
        viewModelScope.launch {
            when (val result = getCalendarUseCase.invoke(selectedDate)) {
                is Result.Success -> {
                    _calendar.value = result.resultData
                }
                is Result.Error -> {
                    Log.e("Calendar", result.exception.message.toString())
                }
            }
        }
    }

    fun increaseDate() {
        selectedDate = selectedDate.plusMonths(1)
    }

    fun decreaseDate() {
        selectedDate = selectedDate.minusMonths(1)
    }

    fun saveDay(text: String, isWorkDay: Boolean) {
        viewModelScope.launch {
            val time = LocalDate.of(getYearFromDate().toInt(), getMonthFromDateInt(), text.toInt())
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
package com.basiatish.data.repositories.sicklistrepository

import com.basiatish.data.api.BiaApi
import com.basiatish.data.mappers.SickMapper
import com.basiatish.domain.common.Result
import com.basiatish.domain.entities.SickListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId

class SickListRemoteSourceImpl(
    private val service: BiaApi,
    private val mapper: SickMapper
) : SickListRemoteSource {

    override suspend fun saveRange(start: Long, end: Long): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                var range = createDate(start)
                val startDate = range
                var endDate = startDate
                if (start != end) {
                    range += "-"
                    val buf = createDate(end)
                    range += buf
                    endDate = buf
                }
                val response = service
                    .saveRange(range.replace(".", ""), mapper.toSickRemote(start, end, startDate, endDate, false))
                if (response.isSuccessful) {
                    return@withContext Result.Success("Success")
                } else {
                    return@withContext Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun getSickList(): Result<List<SickListItem>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getSickList()
                if (response.isSuccessful) {
                    val list = mutableListOf<SickListItem>()
                    response.body()!!.forEach {
                        if (!it.value.isClosed) list.add(mapper.toSickItem(it.value))
                    }
                    return@withContext Result.Success(list)
                } else {
                    Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun closeSickList(start: String, end: String, isClosed: Boolean): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val range = "${start}-${end}".replace(".","")
                val upload = service.closeSickList(range, mapper.toSickStatus(isClosed))
                if (upload.isSuccessful) {
                    return@withContext Result.Success("Success")
                } else {
                    return@withContext Result.Error(Exception(upload.message()))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private fun createDate(timeMills: Long): String {
        var result = ""
        val time = Instant.ofEpochMilli(timeMills)
            .atZone(ZoneId.systemDefault())
        val day = time.dayOfMonth
        val mon = YearMonth.from(time).monthValue
        val year = Year.from(time).value

        if (day < 10) result += "0${day}."
        else result += "$day."

        if (mon < 10) result += "0${mon}."
        else result += "${mon}."

        result += "$year"

        return result
    }
}
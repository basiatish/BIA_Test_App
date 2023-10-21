package com.basiatish.data.api

import com.basiatish.data.api.entities.DayEntityHeaderRemote
import com.basiatish.data.api.entities.IncidentRemote
import com.basiatish.data.api.entities.ProfileRemote
import com.basiatish.data.api.entities.SaveDayRemote
import com.basiatish.data.api.entities.SickListStatus
import com.basiatish.data.api.entities.SickRemote
import com.basiatish.data.api.entities.TaskRemote
import com.basiatish.data.api.entities.TaskStatusRemote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface BiaApi {

    @GET("user/tasks.json")
    suspend fun getTaskList(): Response<List<TaskRemote>>

    @GET("user/tasks/{id}.json")
    suspend fun getTask(@Path("id") id: Int): Response<TaskRemote>

    @PUT("user/incidents/{taskID}.json")
    suspend fun uploadTaskIncident(@Path("taskID") taskID: Int,
                                   @Body incidentRemote: IncidentRemote): Response<IncidentRemote>

    @GET("user/incidents/{taskID}.json")
    suspend fun getTaskIncident(@Path("taskID") taskID: Int): Response<IncidentRemote>

    @PATCH("user/tasks/{id}.json")
    suspend fun updateTaskStatus(@Path("id") id: Int,
                                 @Body statusRemote: TaskStatusRemote): Response<TaskStatusRemote>

    @GET("user/timetable.json")
    suspend fun getCalendar(): Response<DayEntityHeaderRemote>

    @PUT("user/timetable/days/{time}.json")
    suspend fun saveDay(@Path("time") time: String,
        @Body day: SaveDayRemote): Response<SaveDayRemote>

    @GET("user/profile.json")
    suspend fun getProfile(): Response<ProfileRemote>

    @PUT("user/sicklist/{range}.json")
    suspend fun saveRange(@Path("range") range: String,
                          @Body item: SickRemote) : Response<SickRemote>

    @GET("user/sicklist.json")
    suspend fun getSickList(): Response<Map<String, SickRemote>?>

    @PATCH("user/sicklist/{range}.json")
    suspend fun closeSickList(@Path("range") range: String,
                                 @Body status: SickListStatus): Response<SickListStatus>
}
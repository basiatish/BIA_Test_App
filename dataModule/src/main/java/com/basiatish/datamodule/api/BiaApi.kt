package com.basiatish.datamodule.api

import com.basiatish.datamodule.api.entities.IncidentRemote
import com.basiatish.datamodule.api.entities.TaskRemote
import com.basiatish.datamodule.api.entities.TaskStatusRemote
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface BiaApi {

    @GET("tasks.json")
    suspend fun getTaskList(): Response<List<TaskRemote>>

    @GET("tasks/{id}.json")
    suspend fun getTask(@Path("id") id: Int): Response<TaskRemote>

    @PUT("incidents/{taskID}.json")
    suspend fun uploadTaskIncident(@Path("taskID") taskID: Int,
                                   @Body incidentRemote: IncidentRemote): Response<IncidentRemote>

    @GET("incidents/{taskID}.json")
    suspend fun getTaskIncident(@Path("taskID") taskID: Int): Response<IncidentRemote>

    @PATCH("tasks/{id}.json")
    suspend fun updateTaskStatus(@Path("id") id: Int,
                                 @Body statusRemote: TaskStatusRemote): Response<TaskStatusRemote>
}
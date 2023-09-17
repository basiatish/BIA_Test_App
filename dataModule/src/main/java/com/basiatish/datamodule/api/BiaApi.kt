package com.basiatish.datamodule.api

import com.basiatish.datamodule.api.entities.TaskRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BiaApi {

    @GET("v4/weather/realtime")
    suspend fun getTaskList() : Response<List<TaskRemote>>

    @GET("v4/weather/realtime")
    suspend fun getTask(@Query(value = "id") id: Int) : Response<TaskRemote>

}
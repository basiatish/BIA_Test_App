package com.basiatish.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {

    private val gson by lazy {
        val gsonBuilder = GsonBuilder()
            .create()
        gsonBuilder
    }

    private val interceptor by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor
    }

    private val httpClient by lazy {
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private fun getRetrofit(endpointURL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpointURL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createApi(endpointURL: String): BiaApi {
        val retrofit = getRetrofit(endpointURL)
        return retrofit.create(BiaApi::class.java)
    }

}
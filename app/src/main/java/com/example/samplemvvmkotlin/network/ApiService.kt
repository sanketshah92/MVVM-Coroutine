package com.example.samplemvvmkotlin.network

import com.example.samplemvvmkotlin.dashboardmodule.data.DetailsData
import com.example.samplemvvmkotlin.loginmodule.data.UserData
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun doLogin(): UserData

    @GET("topstories.json")
    suspend fun getIds(): List<Long>

    @GET("item/{id}.json")
    suspend fun getIdDetails(@Path("id") id: String): DetailsData

}
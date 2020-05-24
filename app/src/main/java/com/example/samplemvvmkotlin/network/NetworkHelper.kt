package com.example.samplemvvmkotlin.network

class NetworkHelper(private val apiService: ApiService) {
    suspend fun doLogin() = apiService.doLogin()
    suspend fun getIds() = apiService.getIds()
    suspend fun getDetail(id:String) = apiService.getIdDetails(id)
}
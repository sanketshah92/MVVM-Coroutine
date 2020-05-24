package com.example.samplemvvmkotlin.dashboardmodule.datasource

import com.example.samplemvvmkotlin.network.NetworkHelper

class DashboardRepository(private val apiHelper: NetworkHelper) {
    suspend fun getIds() = apiHelper.getIds()
    suspend fun getDetail(id:String) = apiHelper.getDetail(id)
}
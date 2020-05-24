package com.example.samplemvvmkotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samplemvvmkotlin.dashboardmodule.datasource.DashboardRepository
import com.example.samplemvvmkotlin.dashboardmodule.viewmodel.DashboardViewModel
import com.example.samplemvvmkotlin.loginmodule.datasource.LoginRepositiory
import com.example.samplemvvmkotlin.loginmodule.viewmodel.LoginViewModel
import com.example.samplemvvmkotlin.network.NetworkHelper

class ViewModelFactory(private val apiHelper: NetworkHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepositiory(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(DashboardRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
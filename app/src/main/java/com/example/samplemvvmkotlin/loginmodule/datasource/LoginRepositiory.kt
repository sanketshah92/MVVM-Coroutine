package com.example.samplemvvmkotlin.loginmodule.datasource

import com.example.samplemvvmkotlin.loginmodule.data.LoginRequest
import com.example.samplemvvmkotlin.loginmodule.data.UserData
import com.example.samplemvvmkotlin.network.NetworkHelper

class LoginRepositiory(private val apiHelper: NetworkHelper) {
    suspend fun doLogin(loginPayLoad: LoginRequest):UserData{
        return if (loginPayLoad.username.equals("test@worldofplay.in",ignoreCase = true)&& loginPayLoad.password.equals("Worldofplay@2020",ignoreCase = false)){
            UserData(token = "VwvYXBpXC9",statusCode = 200)
        }else{
            UserData(error = "invalid_credentials",statusCode = 401,description = "Email address and password is not a valid combination.")
        }
    }
}
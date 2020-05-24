package com.example.samplemvvmkotlin.loginmodule.data

data class UserData(val statusCode:Int,val token: String="", val error: String="", val description: String="") {
}
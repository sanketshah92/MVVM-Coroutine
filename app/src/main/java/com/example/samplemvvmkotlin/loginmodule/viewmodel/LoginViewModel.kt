package com.example.samplemvvmkotlin.loginmodule.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.samplemvvmkotlin.loginmodule.data.LoginRequest
import com.example.samplemvvmkotlin.loginmodule.datasource.LoginRepositiory
import com.example.samplemvvmkotlin.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginViewModel(private val loginRepositiory: LoginRepositiory) : ViewModel() {
    val userEmail: MutableLiveData<String> = MutableLiveData("")
    val userPwd: MutableLiveData<String> = MutableLiveData("")
    val isValid: MutableLiveData<Boolean> = MutableLiveData(false)

    fun validateInput(email: String, pwd: String): Boolean {
        if (email.trim().isNotEmpty()) {
            if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                if (pwd.isNotEmpty()) {
                    val passwordPattern = "^(?=.*[@$%&#_()=+?»«<>£§€{}\\[\\]-])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*(?<=.{8}$)"
                    val pattern = Pattern.compile(passwordPattern)
                    val matcher: Matcher = pattern.matcher(pwd)
                    if (matcher.matches()) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun updateLoginState(email: String, pwd: String) {
        isValid.value = validateInput(email, pwd)
    }

    fun getLogin() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val loginRequest = LoginRequest(userEmail.value!!, userPwd.value!!)
            val userData = loginRepositiory.doLogin(loginRequest)
            if (userData.statusCode == 200) {
                emit(Resource.success(data = userData))
            } else {
                emit(Resource.error(data = null, message = userData.description))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
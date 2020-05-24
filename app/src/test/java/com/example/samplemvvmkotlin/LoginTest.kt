package com.example.samplemvvmkotlin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samplemvvmkotlin.loginmodule.data.LoginRequest
import com.example.samplemvvmkotlin.loginmodule.data.UserData
import com.example.samplemvvmkotlin.loginmodule.datasource.LoginRepositiory
import com.example.samplemvvmkotlin.loginmodule.viewmodel.LoginViewModel
import com.example.samplemvvmkotlin.network.ApiService
import com.example.samplemvvmkotlin.network.NetworkHelper
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginTest {

    @Mock
    lateinit var loginRepository: LoginRepositiory
    @Mock
    lateinit var apiHelper: NetworkHelper
    @Mock
    lateinit var apiService: ApiService

    lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(loginRepository)
    }


    @Test
    fun validate_invalid_login_input() {
        assertFalse(loginViewModel.validateInput("test@test", "12323"))
    }

    @Test
    fun validate_valid_login_input() {
        assertTrue(loginViewModel.validateInput(email = "test@worldofplay.in", pwd = "Worldofplay@2020"))
    }

    @Test
    fun invalid_request_login() {
        val loginRequest: LoginRequest = LoginRequest("test@test.com", "Test@12345")
        var userData: UserData? = null
        testCoroutineRule.runBlockingTest {
            loginRepository = LoginRepositiory(apiHelper)
            userData = loginRepository.doLogin(loginRequest)
        }
        assertTrue(userData?.statusCode == 401)
    }

    @Test
    fun valid_request_login() {
        val loginRequest: LoginRequest = LoginRequest("test@worldofplay.in", "Worldofplay@2020")
        var userData: UserData? = null
        testCoroutineRule.runBlockingTest {
            loginRepository = LoginRepositiory(apiHelper)
            userData = loginRepository.doLogin(loginRequest)
        }
        assertTrue(userData?.statusCode == 200)
    }

}
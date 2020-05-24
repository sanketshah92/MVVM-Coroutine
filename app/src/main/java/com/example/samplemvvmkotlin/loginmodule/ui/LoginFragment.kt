package com.example.samplemvvmkotlin.loginmodule.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.samplemvvmkotlin.R
import com.example.samplemvvmkotlin.ViewModelFactory
import com.example.samplemvvmkotlin.databinding.FragmentLoginBinding
import com.example.samplemvvmkotlin.loginmodule.viewmodel.LoginViewModel
import com.example.samplemvvmkotlin.network.NetworkHelper
import com.example.samplemvvmkotlin.network.RetrofitBuilder
import com.example.samplemvvmkotlin.network.Status
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    lateinit var loginBinding: FragmentLoginBinding
    lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        : View? {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        //loginBinding.userDetail = UserDetail()
        loginViewModel = ViewModelProviders.of(this, ViewModelFactory(NetworkHelper(RetrofitBuilder.apiService))).get(LoginViewModel::class.java)
        loginBinding.loginViewModel = loginViewModel
        loginBinding.executePendingBindings()
        loginBinding.lifecycleOwner = this
        return loginBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edtEmail.addTextChangedListener(textWatcher)
        edtPwd.addTextChangedListener(textWatcher)
        btnLogin.setOnClickListener {
            processLogin()
        }
    }

    private fun processLogin() {
        progress_login.visibility = View.VISIBLE
        loginViewModel.getLogin().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
                        loginBinding.root.findNavController().navigate(action)
                        progress_login.visibility = View.GONE

                    }
                    Status.ERROR -> {
                        progress_login.visibility = View.GONE
                        Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> progress_login.visibility = View.VISIBLE
                }
            }
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            loginViewModel.updateLoginState(edtEmail.text.toString(), edtPwd.text.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
}
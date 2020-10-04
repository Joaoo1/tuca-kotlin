package com.joaovitor.tucaprodutosdelimpeza.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joaovitor.tucaprodutosdelimpeza.data.LoginRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email = MutableLiveData("")
    var password = MutableLiveData("")

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean>
        get() = _error

    private val _navigateToMain = MutableLiveData<Boolean>(false)
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private var _showProgressBar = MutableLiveData<Boolean>(false)
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    fun onClickLoginButton() {
        login()
    }

    fun login() {
        if (!isUserNameValid(email.value!!) || !isPasswordValid(password.value!!)) {
            _error.value = true
        } else {
            GlobalScope.launch {
                _showProgressBar.postValue(true)

                val result = LoginRepository(getApplication()).login(email = email.value!!, password = password.value!!)

                if(result is Result.Success) {
                    _navigateToMain.postValue(true)
                } else {
                    _error.postValue(true)
                }

                _showProgressBar.postValue(false)
            }
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 6
    }

    fun doneNavigating() {
        _navigateToMain.value = false
    }

    fun doneShowError() {
        _error.value = false
    }

}
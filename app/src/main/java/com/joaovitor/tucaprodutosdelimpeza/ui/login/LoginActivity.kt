package com.joaovitor.tucaprodutosdelimpeza.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.ActivityLoginBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create the View Model
        val viewModelFactory = LoginViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        viewModel.navigateToMain.observe(this) {
            if (it) {
                hideKeyboard(this, currentFocus ?: View(this))
                startActivity(Intent(this, MainActivity::class.java))
                viewModel.doneNavigating()
            }
        }

        viewModel.error.observe(this) {
            if(it) {
                toastLong("Email e/ou senha inválidos!")
                viewModel.doneShowError()
            }
        }

        viewModel.showProgressBar.observe(this) {
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        val binding: ActivityLoginBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_login, null, false)
        binding.viewModel = viewModel

        setContentView(binding.root)
    }
}

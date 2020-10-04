package com.joaovitor.tucaprodutosdelimpeza.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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

package com.joaovitor.tucaprodutosdelimpeza.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.ActivityLoginBinding
import com.joaovitor.tucaprodutosdelimpeza.util.toastLong

class LoginActivity : AppCompatActivity() {

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow((currentFocus?.windowToken ?: "") as IBinder?, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create the View Model
        val viewModelFactory = LoginViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        viewModel.navigateToMain.observe(this) {
            if (it) {
                hideKeyboard()
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

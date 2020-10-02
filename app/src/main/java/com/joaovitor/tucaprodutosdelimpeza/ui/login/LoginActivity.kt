package com.joaovitor.tucaprodutosdelimpeza.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.MainActivity
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create the View Model
        val viewModelFactory = LoginViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        viewModel.navigateToMain.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                viewModel.doneNavigating()
            }
        })

        val binding: ActivityLoginBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_login, null, false)
        binding.viewModel = viewModel

        setContentView(binding.root)
    }
}

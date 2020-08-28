package com.joaovitor.tucaprodutosdelimpeza.ui.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaovitor.tucaprodutosdelimpeza.R

class ClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
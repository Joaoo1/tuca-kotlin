package com.joaovitor.tucaprodutosdelimpeza.ui.sale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaovitor.tucaprodutosdelimpeza.R

class SaleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
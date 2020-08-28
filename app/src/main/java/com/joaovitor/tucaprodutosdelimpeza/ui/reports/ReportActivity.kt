package com.joaovitor.tucaprodutosdelimpeza.ui.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaovitor.tucaprodutosdelimpeza.R

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
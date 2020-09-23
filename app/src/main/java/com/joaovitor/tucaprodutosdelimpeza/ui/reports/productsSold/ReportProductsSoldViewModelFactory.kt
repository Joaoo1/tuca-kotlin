package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReportProductsSoldViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportProductsSoldViewModel::class.java)) {
            return ReportProductsSoldViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
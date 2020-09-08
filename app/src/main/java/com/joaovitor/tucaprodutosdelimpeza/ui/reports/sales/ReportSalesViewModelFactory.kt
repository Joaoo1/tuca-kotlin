package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReportSalesViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportSalesViewModel::class.java)) {
            return ReportSalesViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
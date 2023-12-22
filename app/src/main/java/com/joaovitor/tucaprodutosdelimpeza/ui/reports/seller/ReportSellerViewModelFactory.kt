package com.joaovitor.tucaprodutosdelimpeza.ui.reports.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReportSellerViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportSellerViewModel::class.java)) {
            return ReportSellerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
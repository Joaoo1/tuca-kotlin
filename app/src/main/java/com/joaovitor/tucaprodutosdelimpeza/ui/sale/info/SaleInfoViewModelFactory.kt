package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaleInfoViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleInfoViewModel::class.java)) {
            return SaleInfoViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
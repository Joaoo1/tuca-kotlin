package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaleAddViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleAddViewModel::class.java)) {
            return SaleAddViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
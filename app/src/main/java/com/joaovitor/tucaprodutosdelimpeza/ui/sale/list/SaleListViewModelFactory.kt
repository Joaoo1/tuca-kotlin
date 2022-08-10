package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaleListViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleListViewModel::class.java)) {
            return SaleListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale

class SaleInfoViewModelFactory(private val sale: Sale) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleInfoViewModel::class.java)) {
            return SaleInfoViewModel(sale) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
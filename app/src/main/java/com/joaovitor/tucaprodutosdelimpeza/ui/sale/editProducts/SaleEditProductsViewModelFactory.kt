package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale

class SaleEditProductsViewModelFactory(private val sale: Sale) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleEditProductsViewModel::class.java)) {
            return SaleEditProductsViewModel(sale) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
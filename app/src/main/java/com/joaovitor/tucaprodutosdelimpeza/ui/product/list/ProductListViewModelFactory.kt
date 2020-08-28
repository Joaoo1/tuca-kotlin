package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductListViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
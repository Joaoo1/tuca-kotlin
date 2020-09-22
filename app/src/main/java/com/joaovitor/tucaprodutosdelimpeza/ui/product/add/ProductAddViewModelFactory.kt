package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductAddViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductAddViewModel::class.java)) {
            return ProductAddViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
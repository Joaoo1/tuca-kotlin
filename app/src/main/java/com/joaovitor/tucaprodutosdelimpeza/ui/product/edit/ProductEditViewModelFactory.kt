package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product

class ProductEditViewModelFactory(val product: Product) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductEditViewModel::class.java)) {
            return ProductEditViewModel(product) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
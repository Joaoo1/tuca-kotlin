package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockMovements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StockMovementsViewModelFactory(private val productId: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockMovementsViewModel::class.java)) {
            return StockMovementsViewModel(productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
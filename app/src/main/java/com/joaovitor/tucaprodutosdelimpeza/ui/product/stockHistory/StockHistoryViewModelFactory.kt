package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StockHistoryViewModelFactory(private val productId: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockHistoryViewModel::class.java)) {
            return StockHistoryViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
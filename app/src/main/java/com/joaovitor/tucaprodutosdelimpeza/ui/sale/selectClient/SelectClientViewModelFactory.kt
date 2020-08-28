package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SelectClientViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectClientViewModel::class.java)) {
            return SelectClientViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
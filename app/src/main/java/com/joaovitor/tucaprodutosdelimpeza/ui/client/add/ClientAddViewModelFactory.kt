package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientAddViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientAddViewModel::class.java)) {
            return ClientAddViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
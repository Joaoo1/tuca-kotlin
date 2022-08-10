package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientListViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientListViewModel::class.java)) {
            return ClientListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
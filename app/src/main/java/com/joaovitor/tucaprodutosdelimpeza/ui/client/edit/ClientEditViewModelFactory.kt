package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client

class ClientEditViewModelFactory(private val client: Client) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientEditViewModel::class.java)) {
            return ClientEditViewModel(client) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client

class ClientInfoViewModelFactory(private val client: Client) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientInfoViewModel::class.java)) {
            return ClientInfoViewModel(client) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
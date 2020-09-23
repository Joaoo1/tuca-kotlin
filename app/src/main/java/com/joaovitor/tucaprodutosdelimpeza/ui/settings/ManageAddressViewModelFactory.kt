package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageAddressViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ManageAddressViewModel::class.java)) {
            return ManageAddressViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.joaovitor.tucaprodutosdelimpeza.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _navigateToProduct = MutableLiveData<Boolean?>()
    val navigateToProduct: LiveData<Boolean?> get() = _navigateToProduct

    private val _navigateToClient = MutableLiveData<Boolean?>()
    val navigateToClient: LiveData<Boolean?> get() = _navigateToClient

    private val _navigateToSale = MutableLiveData<Boolean?>()
    val navigateToSale: LiveData<Boolean?> get() = _navigateToSale

    private val _navigateToReport = MutableLiveData<Boolean?>()
    val navigateToReport: LiveData<Boolean?> get() = _navigateToReport

    fun navigateToProduct() {
        _navigateToProduct.value = true
    }

    fun navigateToClient() {
        _navigateToClient.value = true
    }

    fun navigateToSale() {
        _navigateToSale.value = true
    }

    fun navigateToReport() {
        _navigateToReport.value = true
    }

    fun doneNavigating() {
        _navigateToProduct.value = false
        _navigateToClient.value = false
        _navigateToSale.value = false
        _navigateToReport.value = false
    }
}
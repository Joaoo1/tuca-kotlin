package com.joaovitor.tucaprodutosdelimpeza.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.DashboardRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.GeneralInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var generalInfo = MutableLiveData(GeneralInfo())

    private val _navigateToProduct = MutableLiveData<Boolean>()
    val navigateToProduct: LiveData<Boolean> get() = _navigateToProduct

    private val _navigateToClient = MutableLiveData<Boolean>()
    val navigateToClient: LiveData<Boolean> get() = _navigateToClient

    private val _navigateToSale = MutableLiveData<Boolean>()
    val navigateToSale: LiveData<Boolean> get() = _navigateToSale

    private val _navigateToAddSale = MutableLiveData<Boolean>()
    val navigateToAddSale: LiveData<Boolean> get() = _navigateToAddSale

    private val _navigateToReport = MutableLiveData<Boolean>()
    val navigateToReport: LiveData<Boolean> get() = _navigateToReport

    init {
        GlobalScope.launch {
            val result = DashboardRepository().getGeneralInfo()
            if(result is Result.Success) {
                generalInfo.postValue(result.data)
            }
        }
    }

    /* User actions */
    fun onClickProductsButton() {
        _navigateToProduct.value = true
    }

    fun onClickClientsButton() {
        _navigateToClient.value = true
    }

    fun onClickSalesButton() {
        _navigateToSale.value = true
    }

    fun onClickFab() {
        _navigateToAddSale.value = true
    }

    fun onClickReportsButton() {
        _navigateToReport.value = true
    }

    fun doneNavigating() {
        _navigateToProduct.value = false
        _navigateToClient.value = false
        _navigateToSale.value = false
        _navigateToReport.value = false
        _navigateToAddSale.value = false
    }

    /* Database functions*/
    fun onClickUpdateGeneralInfo() {
        GlobalScope.launch {
            val result = DashboardRepository().updateGeneralInfo()
            if(result is Result.Success) {
                generalInfo.postValue(result.data)
            }
        }
    }
}
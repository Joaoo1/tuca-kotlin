package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product

class ReportSalesViewModel : ViewModel() {

    var emptyList: List<Product> = emptyList()
    var products = MutableLiveData(emptyList)

    private var _openDialog = MutableLiveData<Boolean>()
    val openDialog: LiveData<Boolean>
        get() = _openDialog

    fun doneNavigation(){
        _openDialog.value = false
    }

    fun onButtonFilterClicked() {
       _openDialog.value = true
    }
}

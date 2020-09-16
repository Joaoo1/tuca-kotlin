package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleInfoViewModel(mSale: Sale?) : ViewModel() {

    var sale = MutableLiveData(Sale())

    init {
        sale.postValue(mSale)
    }

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd


    private var _navigateToInfo = MutableLiveData<Sale>()
    val navigateToInfo: LiveData<Sale>
        get() = _navigateToInfo


    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfo.value = sale
    }

}

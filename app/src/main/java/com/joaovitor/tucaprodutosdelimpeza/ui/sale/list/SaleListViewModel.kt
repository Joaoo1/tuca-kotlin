package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleListViewModel : ViewModel() {

    private var emptyList: List<Sale> = emptyList()
    var sales = MutableLiveData(emptyList)

    private var saleRepository: SaleRepository = SaleRepository()

    fun setSales() {
        GlobalScope.launch {
            sales.postValue(saleRepository.getSales())
        }
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

package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import java.util.Date

class SaleListViewModel : ViewModel() {

    var emptyList: List<Sale> = emptyList()
    var sales = MutableLiveData(emptyList)

    fun setSales() {
        val mySales = mutableListOf<Sale>()
        mySales.add(Sale(2312, Client("Amanda Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Joaquim Rosa"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Fernando Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Amanda Machado"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("João Silva"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Amanda Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Joaquim Rosa"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Fernando Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Amanda Machado"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("João Silva"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Amanda Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Joaquim Rosa"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Fernando Silveira"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("Amanda Machado"), Date(), "R$20.00"))
        mySales.add(Sale(2312, Client("João Silva"), Date(), "R$20.00"))

        sales.postValue(mySales)
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

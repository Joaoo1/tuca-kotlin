package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import java.util.Date

class SaleAddViewModel : ViewModel() {

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

    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _selectedClient = MutableLiveData<Sale>()
    val selectedClient: LiveData<Sale>
        get() = _selectedClient

    fun onClickFab(){
        _navigateToSelectClient.value = true
    }

    fun doneNavigation(){
        _navigateToSelectClient.value = false
    }

    fun onSaleClicked(sale: Sale) {
        _selectedClient.value = sale
    }

}

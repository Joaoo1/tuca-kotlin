package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.*
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientInfoViewModel(mClient: Client) : ViewModel() {

    var client = MutableLiveData(mClient)
    var clientSales:MutableLiveData<List<Sale>> = MutableLiveData(emptyList())

    private var _navigateToEditClient = MutableLiveData<Client?>()
    val navigateToEditClient: LiveData<Client?>
        get() = _navigateToEditClient

    private var _navigateToInfoSale = MutableLiveData<Sale?>()
    val navigateToInfoSale: LiveData<Sale?>
        get() = _navigateToInfoSale

    init {
        fetchClientSales()
    }

    private fun fetchClientSales() {
        GlobalScope.launch {
            clientSales.postValue(SaleRepository().getSalesByClient(client.value!!.id))
        }
    }
    fun onClickEditClient() {
        _navigateToEditClient.value = client.value
    }

    fun onClickFinishSale(sale: Sale) {
        if(sale.paid){
            //TODO: Show a error message: A paid sale can't be finished
            return
        }

        GlobalScope.launch {
            sale.finishSale()
            val result = SaleRepository().editSale(sale)
            if (result is Result.Success){
                //TODO: Show a success message
                fetchClientSales()
            }else {
                //TODO: Show a error message
                return@launch
            }
        }
    }

    fun onClickInfoSale(sale: Sale) {
        _navigateToInfoSale.value = sale
    }

    fun doneNavigation(){
        _navigateToEditClient.value = null
        _navigateToInfoSale.value = null
    }

}

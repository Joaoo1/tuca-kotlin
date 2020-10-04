package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientInfoViewModel(mClient: Client) : BaseViewModel() {

    var client = MutableLiveData(mClient)
    var clientSales:MutableLiveData<List<Sale>> = MutableLiveData(emptyList())

    var notFoundSales = MutableLiveData(false)

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
            _showProgressBar.postValue(true)

            val resultClientSales = SaleRepository().getSalesByClient(client.value!!.id)
            if (resultClientSales is Result.Success) {
                val data = resultClientSales.data!!
                clientSales.postValue(data)
                if(data.isEmpty()) notFoundSales.postValue(true)
            } else {
                super._error.postValue("Erro ao buscar vendas do cliente")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickEditClient() {
        _navigateToEditClient.value = client.value
    }

    fun onClickFinishSale(sale: Sale) {
        if(sale.paid){
            super._info.postValue("Está venda já está paga")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            sale.finishSale()
            val result = SaleRepository().editSale(sale)
            if (result is Result.Success){
                super._info.postValue("Venda finalizada com sucesso")
                fetchClientSales()
            }else {
                super._error.postValue("Erro ao buscar vendas do cliente")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickInfoSale(sale: Sale) {
        _navigateToInfoSale.value = sale
    }

    fun doneNavigating(){
        _navigateToEditClient.value = null
        _navigateToInfoSale.value = null
    }

    fun onClickCallClient() {
        _error.value = "Função indisponível"
    }

}

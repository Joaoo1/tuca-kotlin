package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class SaleAddViewModel : ViewModel() {

    private var _products = MutableLiveData<List<ProductSale>>()
    val products: LiveData<List<ProductSale>>
        get() = _products

    private var _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>>
        get() = _clients

    val selectedClient = MutableLiveData<Client>()

    init {
        GlobalScope.launch {
            val clientRepository = ClientRepository()
            _clients.postValue(clientRepository.getClients())
        }
    }

    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _navigateBackToAdd = MutableLiveData<Boolean>()
    val navigateBackToAdd: LiveData<Boolean>
        get() = _navigateBackToAdd

    fun navigateToSelectClient(){
        _navigateToSelectClient.value = true
    }

    fun doneNavigation(){
        _navigateToSelectClient.value = false
        _navigateBackToAdd.value = false

    }

    fun onClientClicked(client: Client){
        _navigateBackToAdd.value = true
        selectedClient.postValue(client)
    }
}

package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SaleAddViewModel : ViewModel() {

    //All options available for AutoCompleteTextView
    private val _allProducts =  MutableLiveData<List<Product>>()
    val allProducts: MutableLiveData<List<String>?>
        get() = MutableLiveData(_allProducts.value?.map { it.name })

    private var _sale = MutableLiveData<Sale>(Sale())
    val sale: LiveData<Sale>
        get() = _sale


    init {
        _sale.value?.saleDate = Date()
    }

    private var _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>>
        get() = _clients

    val quantity = MutableLiveData(1)

    init {
        GlobalScope.launch {
            val productRepository = ProductRepository()
            _allProducts.postValue(productRepository.getProducts())

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
        bindClientOnSale(client)
    }

    private fun bindClientOnSale(client: Client) {
        sale.value?.clientName = client.name
        sale.value?.clientNeighborhood = client.neighborhood
        sale.value?.clientStreet = client.name
        sale.value?.clientCity = client.name
        sale.value?.clientComplement = client.name
        sale.value?.clientId = client.name
        sale.value?.clientPhone = client.phone
    }

    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }

    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }

    fun onSaleDateSelect(millis: Long) {
        //FIXME Observer is not called with setValue
        val mSale = _sale.value
        mSale?.saleDate = Date(millis)
        _sale.postValue(mSale)
    }

}

package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleAddViewModel : ViewModel() {

    //All options available for AutoCompleteTextView
    private val _allProducts =  MutableLiveData<List<Product>>()
    val allProducts: MutableLiveData<List<String>?>
        get() = MutableLiveData(_allProducts.value?.map { it.name })


    private var _saleProducts = MutableLiveData<List<ProductSale>>()
    val saleProducts: LiveData<List<ProductSale>>
        get() = _saleProducts

    private var _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>>
        get() = _clients

    val selectedClient = MutableLiveData<Client>()

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
        selectedClient.postValue(client)
    }

    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }

    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }
}

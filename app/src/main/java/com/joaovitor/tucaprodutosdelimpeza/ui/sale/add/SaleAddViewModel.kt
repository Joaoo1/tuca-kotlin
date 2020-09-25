package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class SaleAddViewModel : ViewModel() {

    //All options available for AutoCompleteTextView
    private val _allProducts =  MutableLiveData<List<Product>>()
    val allProducts: MutableLiveData<List<Product>>
        get() = _allProducts

    //All clients for SelectClientFragment
    private var _allClients = MutableLiveData<List<Client>>()
    val allClients: LiveData<List<Client>>
        get() = _allClients

    //Fields form
    private var _client = MutableLiveData<Client>(Client())
    val client: LiveData<Client>
        get() = _client

    private var _saleDate = MutableLiveData<Date>(Date())
    val saleDate: LiveData<Date>
        get() = _saleDate

    private var _total = MutableLiveData<BigDecimal>(BigDecimal(0))
    val total: LiveData<BigDecimal>
        get() = _total

    private var _products = MutableLiveData<List<ProductSale>>(emptyList())
    val products = MediatorLiveData<List<ProductSale>>()

    val quantity = MutableLiveData(1)
    val discount = MutableLiveData<String>()
    val paidValue = MutableLiveData<String>()

    var paymentMethod: Int = 0


    init {
        GlobalScope.launch {
            val productRepository = ProductRepository()
            _allProducts.postValue(productRepository.getProducts())

            val clientRepository = ClientRepository()
            _allClients.postValue(clientRepository.getClients())
        }

        //Update total every time _products is changed
        products.addSource(_products) {
            products.value = it
            _total.value = calculateTotalFromProductsList(it)
            quantity.value = 1
        }
    }

/*    private fun bindClientOnSale(client: Client) {
        sale.value?.clientName = client.name
        sale.value?.clientNeighborhood = client.neighborhood
        sale.value?.clientStreet = client.name
        sale.value?.clientCity = client.name
        sale.value?.clientComplement = client.name
        sale.value?.clientId = client.name
        sale.value?.clientPhone = client.phone
    }*/

    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }

    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }

    fun onSaleDateSelect(millis: Long) {
        _saleDate.postValue(Date(millis))
    }

    private fun calculateTotalFromProductsList(products: List<ProductSale>?): BigDecimal {
        if(products != null && products.isNotEmpty()) {
            val productsTotal =
                products.map { BigDecimal(it.price).multiply(BigDecimal(it.quantity)) }
            return productsTotal.reduce { acc, productTotal -> acc.plus(productTotal) }
        }
        return BigDecimal(0)
    }

    // Navigation functions
    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _navigateBackToAdd = MutableLiveData<Boolean>()
    val navigateBackToAdd: LiveData<Boolean>
        get() = _navigateBackToAdd

    fun navigateToSelectClient(){
        _navigateToSelectClient.value = true
    }

    fun onClientClicked(client: Client){
        _navigateBackToAdd.value = true
        _client.postValue(client)
    }

    fun doneNavigation(){
        _navigateToSelectClient.value = false
        _navigateBackToAdd.value = false
    }

    fun addProduct(text: String?) {
        val productsName = allProducts.value?.map{ it.name }
        val productIndex = productsName?.indexOf(text)
        if(productIndex != null && productIndex >= 0) {
            val productSale = convertProductToProductSale(
                _allProducts.value!![productIndex],
                quantity.value!!
            )
            _products.value = _products.value?.plus(productSale)
        } else {
            //TODO: Show a error
            return
        }
    }

    private fun convertProductToProductSale(product: Product, quantity: Int): ProductSale {
        return ProductSale(product.name, product.price, quantity, Date(), product.id)
    }
}

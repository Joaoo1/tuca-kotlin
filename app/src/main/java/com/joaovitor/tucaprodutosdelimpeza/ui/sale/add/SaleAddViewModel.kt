package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class SaleAddViewModel : ViewModel() {

    private val saleRepository = SaleRepository()

    //All options available for AutoCompleteTextView
    private val _allProducts =  MutableLiveData<List<Product>>()
    val allProducts: MutableLiveData<List<Product>>
        get() = _allProducts

    //All clients for SelectClientFragment
    private var _allClients = MutableLiveData<List<Client>>()
    val allClients: LiveData<List<Client>>
        get() = _allClients

    //Fields form
    private var _client = MutableLiveData<Client>()
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

    var paymentMethod = MutableLiveData<Int>(R.id.radio_button_unpaid)

    val quantity = MutableLiveData(1)

    val discount = MutableLiveData<String>()

    val paidValue = MutableLiveData<String>()

    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _navigateBackToAdd = MutableLiveData<Boolean>()
    val navigateBackToAdd: LiveData<Boolean>
        get() = _navigateBackToAdd

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

    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }

    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }

    private suspend fun addSale() {
        if(validateFields()){
            val sale = Sale()
            sale.products = _products.value!!.toMutableList()
            sale.bindClient(_client.value!!)
            sale.saleDate = _saleDate.value!!
            sale.discount = BigDecimal(discount.value?: "0.00").toString()
            sale.grossValue = calculateTotalFromProductsList(_products.value!!).toString()
            sale.total = _total.value!!.minus(BigDecimal(discount.value?: "0.00")).toString()

            when(paymentMethod.value!!) {
                R.id.radio_button_paid -> {
                    sale.paid = true
                    sale.paymentDate = Date()
                    sale.paidValue = _total.value.toString()
                    sale.toReceive = "0.00"
                }

                R.id.radio_button_unpaid -> {
                    sale.paid = false
                    sale.paymentDate = null
                    sale.paidValue = "0.00"
                    sale.toReceive = _total.value?.minus(BigDecimal(discount.value?:"0.00")).toString()
                }

                R.id.radio_button_partially_paid -> {
                    sale.paid = false
                    sale.paymentDate = null
                    sale.paidValue = BigDecimal(paidValue.value!!).toString()
                    sale.toReceive = _total.value?.
                        minus(BigDecimal(paidValue.value))?.
                        minus(BigDecimal(discount.value?: "0.00")).toString()
                }

                else -> return //TODO: Show a error: Error unknown on payment situation
            }

            //TODO: Set seller and seller UID on sale
            sale.seller = "João Vitor"
            sale.sellerUid = "asd"

            saleRepository.addSale(sale) //TODO: Get result and show a respective message

            reset()
        }
    }

    private fun reset() {
        _products.postValue(emptyList())
        _client.postValue(null)
        _saleDate.postValue(Date())
        discount.postValue(null)
        paidValue.postValue(null)
        paymentMethod.postValue(R.id.radio_button_unpaid)
    }

    /** Check the input fields */
    private fun validateFields(): Boolean {
        if(_products.value!!.isEmpty()) {
            //TODO: Show a message error: No products added
            return false
        }

        if(_client.value == null) {
            //TODO: Show a message error: No selected client
            return false
        }

        if(paymentMethod.value == R.id.radio_button_partially_paid) {
            if(paidValue.value == null || paidValue.value!!.isEmpty()) {
                //TODO: Show a message error: Inform the paid value
                return false
            }

            if(paidValue.value!!.last() == '.' ) {
                //TODO: Show a error message: value invalid
                return false
            }

            if(BigDecimal(paidValue.value!!) >= _total.value) {
                //TODO: Show a message error: Paid value is greater than total
                return false
            }
        }

        if(discount.value != null && discount.value!!.isNotEmpty() && discount.value!!.last() == '.' ) {
            //TODO: Show a error message: price invalid
            return false
        }

        return true
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
    fun navigateToSelectClient(){
        _navigateToSelectClient.value = true
    }

    fun onClientClicked(client: Client){
        _navigateBackToAdd.value = true
        _client.postValue(client)
    }

    fun doneNavigating(){
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

            /**
             * Check if the new product is already on list
             * If so, just increment the quantity of existing product
             * Otherwise, add the product to list
             */
            val mProducts = _products.value!!
            for(product in mProducts) {
                if(productSale.parentId == product.parentId){
                    product.quantity += quantity.value!!
                    _products.value = mProducts
                    return
                }
            }

            _products.value = _products.value?.plus(productSale)
        } else {
            //TODO: Show a error
            return
        }
    }

    private fun convertProductToProductSale(product: Product, quantity: Int): ProductSale {
        return ProductSale(product.name, product.price, quantity, Date(), product.id)
    }

    fun onClickAddSale() {
        GlobalScope.launch {
            addSale()
        }
    }
}

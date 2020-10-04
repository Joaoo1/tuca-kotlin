package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.bluetooth.PrinterFunctions
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.LoginRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class SaleAddViewModel(application: Application) : AndroidViewModel(application) {

    private val saleRepository = SaleRepository()

    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var _info = MutableLiveData<String>()
    val info: LiveData<String>
        get() = _info

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

    private var _saleDate = MutableLiveData(Date())
    val saleDate: LiveData<Date>
        get() = _saleDate

    private var _total = MutableLiveData(BigDecimal(0))
    val total: LiveData<BigDecimal>
        get() = _total

    var printReceipt = MutableLiveData(true)

    private var _products = MutableLiveData<List<ProductSale>>(emptyList())
    val products = MediatorLiveData<List<ProductSale>>()

    var paymentMethod = MutableLiveData(R.id.radio_button_unpaid)

    val quantity = MutableLiveData(1)

    val discount = MutableLiveData<String>()

    val paidValue = MutableLiveData<String>()

    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _navigateBackToAdd = MutableLiveData<Boolean>()
    val navigateBackToAdd: LiveData<Boolean>
        get() = _navigateBackToAdd

    private var _requestBluetoothOn = MutableLiveData(false)
    val requestBluetoothOn: LiveData<Boolean>
        get() = _requestBluetoothOn

    private var _dialogBluetoothOff = MutableLiveData(false)
    val dialogBluetoothOff: LiveData<Boolean>
        get() = _dialogBluetoothOff

    private var _showProgressBar = MutableLiveData<Boolean>(false)
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    var sale = Sale()

    init {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val productRepository = ProductRepository()
            val resultProduct = productRepository.getProducts()
            if(resultProduct is Result.Success) {
                _allProducts.postValue(resultProduct.data)
            } else {
                _error.postValue("Erro ao carregar produtos!")
            }

            val clientRepository = ClientRepository()
            val resultClient = clientRepository.getClients()
            if(resultClient is Result.Success) {
                _allClients.postValue(resultClient.data)
            } else {
                _error.postValue("Erro ao carregar clientes!")
            }

            _showProgressBar.postValue(false)
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

    private fun addSale() {
        if(validateFields()){
            _showProgressBar.postValue(true)

            val mSale = Sale()
            mSale.products = _products.value!!.toMutableList()
            mSale.bindClient(_client.value!!)
            mSale.saleDate = _saleDate.value!!
            mSale.discount = BigDecimal(discount.value?: "0.00").toString()
            mSale.grossValue = calculateTotalFromProductsList(_products.value!!).toString()
            mSale.total = _total.value!!.minus(BigDecimal(discount.value?: "0.00")).toString()

            when(paymentMethod.value!!) {
                R.id.radio_button_paid -> {
                    mSale.paid = true
                    mSale.paymentDate = Date()
                    mSale.paidValue = _total.value.toString()
                    mSale.toReceive = "0.00"
                }

                R.id.radio_button_unpaid -> {
                    mSale.paid = false
                    mSale.paymentDate = null
                    mSale.paidValue = "0.00"
                    mSale.toReceive = _total.value?.minus(BigDecimal(discount.value?:"0.00")).toString()
                }

                R.id.radio_button_partially_paid -> {
                    mSale.paid = false
                    mSale.paymentDate = null
                    mSale.paidValue = BigDecimal(paidValue.value!!).toString()
                    mSale.toReceive = _total.value?.
                    minus(BigDecimal(paidValue.value))?.
                    minus(BigDecimal(discount.value?: "0.00")).toString()
                }

                else -> _error.postValue("Erro desconhecido na situação de pagamento")
            }

            val loginRepository = LoginRepository(getApplication())
            mSale.seller = loginRepository.getCachedUserName()
            mSale.sellerUid = loginRepository.getCachedUserUid()

            GlobalScope.launch {
                val result = saleRepository.addSale(mSale)

                if (result is Result.Success) {
                    _info.postValue("Venda adicionado com sucesso")

                    // Print the receipt for the sale
                    sale = mSale
                    if(printReceipt.value!!) printReceipt()

                    //Clean the form fields
                    reset()
                } else {
                    _error.postValue("Erro ao registrar venda")
                }

                _showProgressBar.postValue(false)
            }
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

    fun addProduct(text: String?) {
        if(text == null || text.isEmpty()) {
            _error.value = "Digite o nome do produto"
            return
        }

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
            _error.postValue("Produto não encontrado")
        }
    }

    private fun convertProductToProductSale(product: Product, quantity: Int): ProductSale {
        return ProductSale(product.name, product.price, quantity, Date(), product.id)
    }

    /** Check the input fields */
    private fun validateFields(): Boolean {
        if(_products.value!!.isEmpty()) {
            _error.postValue("Não há produtos adicionados")
            return false
        }

        if(_client.value == null) {
            _error.postValue("Selecione um cliente")
            return false
        }

        if(paymentMethod.value == R.id.radio_button_partially_paid) {
            if(paidValue.value == null || paidValue.value!!.isEmpty()) {
                _error.postValue("Informe o valor pago")
                return false
            }

            if(paidValue.value!!.last() == '.' ) {
                _error.postValue("O valor pago informado é inválido")
                return false
            }

            if(BigDecimal(paidValue.value!!) >= _total.value) {
                _error.postValue("Valor pago é maior que o total da venda")
                return false
            }
        }

        if(discount.value != null && discount.value!!.isNotEmpty() && discount.value!!.last() == '.' ) {
            _error.postValue("O valor de desconto informado é inválido")
            return false
        }

        if(discount.value != null && discount.value!!.isEmpty()){
            discount.value = "0.00"
        }

        return true
    }

    private fun calculateTotalFromProductsList(products: List<ProductSale>?): BigDecimal {
        if(products != null && products.isNotEmpty()) {
            val productsTotal =
                products.map { BigDecimal(it.price).multiply(BigDecimal(it.quantity)) }

            return productsTotal.reduce { acc, productTotal -> acc.plus(productTotal) }
        }

        return BigDecimal(0)
    }

    /* User actions */
    fun onClickAddSale() {
        addSale()
    }

    fun onSaleDateSelect(millis: Long) {
        _saleDate.postValue(Date(millis))
    }

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
        _dialogBluetoothOff.value = false
        _requestBluetoothOn.value = false
    }

    fun doneShowError() {
        _error.value = null
    }

    fun doneShowInfo() {
        _info.value = null
    }

    /* Printer */
    private fun printReceipt() {
        val printerFunctions = PrinterFunctions(getApplication())

        if (printerFunctions.btAdapter?.isEnabled!!) {
            printerFunctions.printReceipt(sale, sale.products)
        } else {
            _dialogBluetoothOff.value = true
        }
    }

    fun onBluetoothResult(resultCode: Int) {
        if(resultCode == Activity.RESULT_OK) {
            printReceipt()
        }else if (resultCode == Activity.RESULT_CANCELED) {
            _error.value = "Ocorreu um erro ao ativar bluetooth!"
        }
    }

    fun onClickBluetoothDialogPositive() {
        _requestBluetoothOn.value = true
    }

    fun onClickDeleteProduct(position: Int) {
        val list = products.value!!.toMutableList()
        list.removeAt(position - 1)
        _products.value = list
    }
}

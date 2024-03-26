package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StockRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SaleEditProductsViewModel(var mSale: Sale) : BaseViewModel() {

    private var _products = MutableLiveData<MutableList<ProductSale>>()
    val products = MediatorLiveData<MutableList<ProductSale>>()

    private var _total = MutableLiveData(BigDecimal(mSale.grossValue))
    val total: LiveData<BigDecimal>
        get() = _total

    /** List of products used by AutoCompleteTextView on Add Products Dialog*/
    private var _allProducts = MutableLiveData<List<Product>?>()
    val allProducts: LiveData<List<Product>?>
        get() = _allProducts

    val quantity:MutableLiveData<Int> = MutableLiveData(1)

    private var _openAddProductDialog = MutableLiveData(false)
    val openAddProductDialog: LiveData<Boolean>
        get() = _openAddProductDialog

    private var _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    private var addedProducts: MutableList<Int> = mutableListOf()

    init {
        /**
         * Clone product sales with a deep copy,
         * thus avoiding altering the sale object of the infosale page
         * and therefore doesn't showing false information if the user navigate back
         * without saving the information
         */
        _products.value = mSale.products
            .map {it.copy()}
            .toMutableList()
            .map {
                it.isPostAdded = null
                it
            }.toMutableList()


        /** Update @property total every time @property _products is changed */
        products.addSource(_products) {
            products.value = it
            _total.value = calculateTotalFromProductsList(it)
            quantity.value = 1
        }

        GlobalScope.launch {
            val resultProducts = ProductRepository().getProducts()
            if(resultProducts is Result.Success) {
                _allProducts.postValue(resultProducts.data)
            } else {
                _error.postValue("Erro ao carregar produtos!")
            }
        }
    }

    /** Functions that manage the [quantity] value */
    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }
    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }

    /** Add a new product for sale based on [productName] */
    fun addProduct(productName: String?) {
        /*
         * Convert product list to a string list
         * That way, it is possible to use List.indexOf()
         * to validate the product and find its index
         */
        val productsName = allProducts.value?.map{ it.name }
        val productIndex = productsName?.indexOf(productName)

        /* Check if [productName] correspond to a product on products list */
        if (productIndex == null || productIndex == -1) {
            _error.postValue("Produto não encontrado!")
            return
        }

        val product = _allProducts.value!![productIndex]
        val productSale = _allProducts.value!![productIndex].toProductSale(quantity.value!!)

        if(product.manageStock) productSale.isPostAdded = true

        _products.value = _products.value?.plus(productSale)?.toMutableList()

        addedProducts.add(_products.value!!.size - 1)
    }

    private fun calculateTotalFromProductsList(products: List<ProductSale>?): BigDecimal {
        if(products != null && products.isNotEmpty()) {
            val productsTotal =
                products.map { BigDecimal(it.price).multiply(BigDecimal(it.quantity)) }
            return productsTotal.reduce { acc, productTotal -> acc.add(productTotal) }
        }
        return BigDecimal(0)
    }

    fun onClickSave() {
        /** Check if there is added products */
        if(_products.value!!.isEmpty()) {
            _error.postValue("Não há produtos adicionados!")
            return
        }

        //FIXME: Abstract this or change the sale structure
        mSale.products = _products.value!!
        mSale.grossValue = _total.value.toString()
        mSale.total = _total.value!!.minus(BigDecimal(mSale.discount)).toString()
        mSale.toReceive = _total.value?.minus(BigDecimal(mSale.paidValue)).toString()

        GlobalScope.launch(Dispatchers.Default) {
            _showProgressBar.postValue(true)

            val result = SaleRepository().editSale(mSale)
            if(result is Result.Success) {
                _info.postValue("Venda editada com sucesso")
                _navigateBack.postValue(true)

                // Register stock movements
                for(product in _products.value!!) {
                    val addedProducts = mutableListOf<ProductSale>()
                    if(product.isPostAdded != null && product.isPostAdded!!) {
                        addedProducts.add(product)
                    }
                    StockRepository().addStockMovement(addedProducts, mSale.saleId)
                }
            } else {
                _error.postValue("Ocorreu um erro ao editar produtos!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickAddProduct(){
        _openAddProductDialog.value = true
    }

    fun doneNavigating(){
        _openAddProductDialog.value = false
        _navigateBack.value = false
    }

    fun removeProductAt(adapterPosition: Int) {
        val list = _products.value!!.toMutableList()
        list.removeAt(adapterPosition)
        _products.value = list
    }
}
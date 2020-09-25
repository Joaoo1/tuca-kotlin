package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SaleEditProductsViewModel(var mSale: Sale) : ViewModel() {

    private var _products = MutableLiveData<MutableList<ProductSale>>()
    val products = MediatorLiveData<MutableList<ProductSale>>()

    private var _total = MutableLiveData<BigDecimal>(BigDecimal(mSale.grossValue))
    val total: LiveData<BigDecimal>
        get() = _total

    /** List of products used by AutoCompleteTextView on Add Products Dialog*/
    private var _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>>
        get() = _allProducts

    val quantity:MutableLiveData<Int> = MutableLiveData(1)

    private var _openAddProductDialog = MutableLiveData(false)
    val openAddProductDialog: LiveData<Boolean>
        get() = _openAddProductDialog

    private var _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    init {
        /**
         * Clone product sales with a deep copy,
         * thus avoiding altering the sale object of the infosale page
         * and therefore doesn't showing false information if the user navigate back
         * without saving the information
         */
        _products.value = mSale.products.map {it.copy()}.toMutableList()

        /** Update @property total every time @property _products is changed */
        products.addSource(_products) {
            products.value = it
            _total.value = calculateTotalFromProductsList(it)
            quantity.value = 1
        }

        GlobalScope.launch {
            _allProducts.postValue(ProductRepository().getProducts())
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
        /**
         * Convert product list to a string list
         * That way, it is possible to use List.indexOf()
         * to validate the product and find its index
         */
        val productsName = allProducts.value?.map{ it.name }
        val productIndex = productsName?.indexOf(productName)

        /** Check if [productName] correspond to a product on products list */
        if (productIndex == null || productIndex == -1) {
            //TODO: Show a error: product not found
            return
        }

        val productSale = _allProducts.value!![productIndex].toProductSale(quantity.value!!)

        /**
         * Check if the new product is already on list
         * If so, just increment the quantity of existing product
         * Otherwise, add the zproduct to list
         */
        val mProducts = _products.value!!
        for(product in mProducts) {
            if(productSale.parentId == product.parentId){
                product.quantity += quantity.value!!
                _products.value = mProducts
                return
            }
        }

        _products.value = _products.value?.plus(productSale)?.toMutableList()
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
            //TODO: Show a error: No products added
            return
        }

        //FIXME: Abstract this or change the sale structure
        mSale.products = _products.value!!
        mSale.grossValue = _total.value.toString()
        mSale.total = _total.value!!.minus(BigDecimal(mSale.discount)).toString()
        mSale.toReceive = _total.value?.minus(BigDecimal(mSale.paidValue)).toString()

        GlobalScope.launch(Dispatchers.Default) {
            SaleRepository().editSale(mSale)
            //TODO: Show a success message: Products added successfully
        }
    }

    fun onClickAddProduct(){
        _openAddProductDialog.value = true
    }

    fun doneNavigation(){
        _openAddProductDialog.value = false
    }
}
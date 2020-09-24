package com.joaovitor.tucaprodutosdelimpeza.ui.sale.editProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleEditProductsViewModel(mSale: Sale) : ViewModel() {

    private var _sale = MutableLiveData<Sale>(mSale)
    val sale: LiveData<Sale>
        get() = _sale

    /** List of products used by AutoCompleteTextView on Add Products Dialog*/
    private var _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>>
        get() = _allProducts

    val quantity = MutableLiveData(1)

    init {
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
         * Converting product list to a string list
         * That way, it is possible to use List.indexOf()
         * to validate the product and find its index
         */
        val productsName = allProducts.value?.map{ it.name }
        val productIndex = productsName?.indexOf(productName)

        /** If the [productName] is not found on products list a error will be show to user */
        if (productIndex == null || productIndex == -1) {
            //TODO: Show a error: product not found
            return
        }

        val productSale = _allProducts.value!![productIndex].toProductSale(quantity.value!!)
        val mSale = sale.value
        mSale?.products?.add(productSale)!!
        _sale.postValue(mSale)
    }
}
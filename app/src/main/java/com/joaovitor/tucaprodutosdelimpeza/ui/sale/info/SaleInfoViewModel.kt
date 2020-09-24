package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SaleInfoViewModel(mSale: Sale?) : ViewModel() {

    private val saleRepository = SaleRepository()

    var sale = MutableLiveData(Sale())

    init {
        sale.postValue(mSale)
    }

    private var _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>>
        get() = _allProducts

    val quantity = MutableLiveData(1)

    fun addQuantity() {
        quantity.postValue(quantity.value?.plus(1))
    }

    fun removeQuantity() {
        if(quantity.value!! > 0) {
            quantity.postValue(quantity.value?.minus(1))
        }
    }

    fun addProduct(text: String?) {
        val productsName = allProducts.value?.map{ it.name }
        val productIndex = productsName?.indexOf(text)
        if(productIndex != null && productIndex >= 0) {
            val productSale = convertProductToProductSale(
                _allProducts.value!![productIndex],
                quantity.value!!
            )
            val mSale = sale.value
            mSale?.products = mSale?.products?.plus(productSale)!!
            sale.value = mSale
        } else {
            //TODO: Show a error
            return
        }
    }

    private fun convertProductToProductSale(product: Product, quantity: Int): ProductSale {
        return ProductSale(product.name, product.price, quantity, Date(), product.id)
    }

    //Database functions
    fun deleteSale() {
        GlobalScope.launch {
            sale.value?.id?.let { saleRepository.deleteSale(it) }
        }
    }

    //Navigation
    private var _openPaymentDialog = MutableLiveData<Boolean>()
    val openPaymentDialog: LiveData<Boolean>
        get() = _openPaymentDialog

    private var _navigateToEditProducts = MutableLiveData<Boolean>()
    val navigateToEditProducts: LiveData<Boolean>
        get() = _navigateToEditProducts

    fun navigateToEditProducts() {
        _navigateToEditProducts.value = true
        GlobalScope.launch {
            _allProducts.postValue(ProductRepository().getProducts())
        }
    }

    fun onClickRegisterPayment() {
        if(!sale.value?.paid!!) _openPaymentDialog.value = true
    }

    fun doneNavigation(){
        _navigateToEditProducts.value = false
    }

    fun registerPayment(value: String) {
        if(value.isEmpty()) {
            GlobalScope.launch {
                sale.value?.finishSale()
                saleRepository.editSale(sale.value!!)
            }
        }else {

        }
    }


}

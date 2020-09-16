package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductEditViewModel(mProduct: Product?) : ViewModel() {

    var _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    init {
        if(mProduct != null) _product.postValue(mProduct)
    }

    private var _navigateToStockHistories = MutableLiveData<Boolean>()
    val navigateToStockHistories: LiveData<Boolean>
        get() = _navigateToStockHistories

    fun doneNavigation(){
        _navigateToStockHistories.value = false
    }

}

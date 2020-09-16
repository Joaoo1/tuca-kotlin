package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    var emptyList: List<Product> = emptyList()
    var products = MutableLiveData(emptyList)

    private var productRepository = ProductRepository()

    init {
        GlobalScope.launch {
            products.postValue(productRepository.getProducts())
        }
    }

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd


    private var _navigateToEdit = MutableLiveData<Product>()
    val navigateToEdit: LiveData<Product>
        get() = _navigateToEdit


    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToEdit.value = null
    }

    fun onProductClicked(product: Product) {
        _navigateToEdit.value = product
    }

}

package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductListViewModel : BaseViewModel() {

    private var _products = MutableLiveData<List<Product>?>()
    val products: LiveData<List<Product>?>
        get() = _products

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToEdit = MutableLiveData<Product?>()
    val navigateToEdit: LiveData<Product?>
        get() = _navigateToEdit

    init {
       fetchProducts()
    }

    private fun fetchProducts() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultProduct = ProductRepository().getProducts()
            if (resultProduct is Result.Success) {
                _products.postValue(resultProduct.data)
            } else {
                _error.postValue("Erro ao carregar produtos!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onProductClicked(product: Product) {
        _navigateToEdit.value = product
    }

    fun doneNavigating(){
        _navigateToAdd.value = false
        _navigateToEdit.value = null
    }

    fun onClickRefreshList() {
        fetchProducts()
    }

}

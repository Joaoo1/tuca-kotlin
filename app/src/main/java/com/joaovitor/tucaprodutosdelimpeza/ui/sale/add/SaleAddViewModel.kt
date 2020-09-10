package com.joaovitor.tucaprodutosdelimpeza.ui.sale.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import java.util.Date

class SaleAddViewModel : ViewModel() {

    var emptyList: List<Product> = emptyList()
    var products = MutableLiveData(emptyList)

    fun setProducts() {
        val myProducts = mutableListOf<Product>()
        myProducts.add(Product("Detergente verde limão","20.00",20,2))
        myProducts.add(Product("Detergente verde limão","20.00",20,2))
        products.postValue(myProducts)
    }

    private var _navigateToSelectClient = MutableLiveData<Boolean>()
    val navigateToSelectClient: LiveData<Boolean>
        get() = _navigateToSelectClient

    private var _selectedClient = MutableLiveData<Sale>()
    val selectedClient: LiveData<Sale>
        get() = _selectedClient

    fun navigateToSelectClient(){
        _navigateToSelectClient.value = true
    }

    fun doneNavigation(){
        _navigateToSelectClient.value = false
    }

}

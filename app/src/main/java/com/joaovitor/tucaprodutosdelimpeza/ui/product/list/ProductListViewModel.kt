package com.joaovitor.tucaprodutosdelimpeza.ui.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product

class ProductListViewModel : ViewModel() {

    var emptyList: List<Product> = emptyList()
    var products = MutableLiveData(emptyList)

    fun setProducts() {
        val myProducts = mutableListOf<Product>()
        myProducts.add(Product("Alvejante", "R$20.00",123, 2))
        myProducts.add(Product("Amaciante", "R$20.00",123, 2))
        myProducts.add(Product("Desinfentante", "R$20.00",123, 2))
        myProducts.add(Product("Detergente neutro", "R$20.00",123, 2))
        myProducts.add(Product("Detergente de coco", "R$20.00",123, 2))
        myProducts.add(Product("Cloro roupa colorida", "R$20.00",123, 2))
        myProducts.add(Product("Sabão Liquido", "R$20.00",123, 2))
        myProducts.add(Product("Vanish", "R$20.00",123, 2))
        myProducts.add(Product("Alvejante", "R$20.00",123, 2))
        myProducts.add(Product("Amaciante", "R$20.00",123, 2))
        myProducts.add(Product("Desinfentante", "R$20.00",123, 2))
        myProducts.add(Product("Detergente neutro", "R$20.00",123, 2))
        myProducts.add(Product("Detergente de coco", "R$20.00",123, 2))
        myProducts.add(Product("Cloro roupa colorida", "R$20.00",123, 2))
        myProducts.add(Product("Sabão Liquido", "R$20.00",123, 2))
        myProducts.add(Product("Vanish", "R$20.00",123, 2))
        myProducts.add(Product("Alvejante", "R$20.00",123, 2))
        myProducts.add(Product("Amaciante", "R$20.00",123, 2))
        myProducts.add(Product("Desinfentante", "R$20.00",123, 2))
        myProducts.add(Product("Detergente neutro", "R$20.00",123, 2))
        myProducts.add(Product("Detergente de coco", "R$20.00",123, 2))
        myProducts.add(Product("Cloro roupa colorida", "R$20.00",123, 2))
        myProducts.add(Product("Sabão Liquido", "R$20.00",123, 2))
        myProducts.add(Product("Vanish", "R$20.00",123, 2))

        products.postValue(myProducts)
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

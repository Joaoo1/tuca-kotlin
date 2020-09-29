package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductEditViewModel(var mProduct: Product) : ViewModel() {

    var product = MutableLiveData<Product>(mProduct.copy())

    fun onClickSaveEditCadaster() {
        if(product.value!!.name.isEmpty() || product.value!!.price.isEmpty()) {
            //TODO: Show error message: fields can't be empty
            return
        }

        GlobalScope.launch {
            val result = ProductRepository().editProduct(product.value!!)

            if(result is Result.Success) {
                //TODO: Show success message
                mProduct.bind(product.value!!)
            }else {
                //TODO: Show error message
                return@launch
            }
        }
    }

    fun onClickSaveEditStock() {
        if(product.value!!.manageStock && product.value!!.stock == 0) {
            //TODO: Show error message: stock can't be zero
            return
        }

        GlobalScope.launch {
            val result = ProductRepository().editProduct(product.value!!)

            if(result is Result.Success) {
                //TODO: Show success message
                mProduct.bind(product.value!!)
            }else {
                //TODO: Show error message
                return@launch
            }
        }
    }

}

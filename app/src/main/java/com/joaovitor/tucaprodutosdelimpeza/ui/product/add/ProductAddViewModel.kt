package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class ProductAddViewModel: ViewModel() {

    var product = MutableLiveData(Product())

    var isManagedStock = MutableLiveData<Boolean>(false)

    private var _navigateBack = MutableLiveData<Boolean>(false)
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun onClickSave() {
        addProduct()
    }

    fun doneNavigation() {
        _navigateBack.value = false
    }
    private fun addProduct() {
        if(product.value!!.name.isEmpty() || product.value!!.price.isEmpty()) {
            //TODO: Show a error message: need to inform fields
            return
        }

        if(product.value!!.price.last() == '.') {
            //TODO: Show a error message: price invalid
            return
        }

        GlobalScope.launch {
            product.value!!.price = BigDecimal(product.value!!.price).setScale(2, RoundingMode.FLOOR).toString()
            val result = ProductRepository().addProduct(product.value!!)
            if(result is Result.Success) {
                //TODO: Show a success message
                _navigateBack.postValue(true)
            }else {
                //TODO: Show a error message
                return@launch
            }
        }
    }

}

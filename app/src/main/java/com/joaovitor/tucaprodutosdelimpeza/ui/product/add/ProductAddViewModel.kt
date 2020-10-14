package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class ProductAddViewModel: BaseViewModel() {

    var product = MutableLiveData(Product())

    var isManagedStock = MutableLiveData(false)

    private var _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun onClickSave() {
        addProduct()
    }

    fun doneNavigating() {
        _navigateBack.value = false
    }

    private fun addProduct() {
        if(product.value!!.name.isEmpty() || product.value!!.price.isEmpty()) {
            _error.postValue("Os campos não podem ficar em branco")
            return
        }

        if(product.value!!.price.last() == '.') {
            _error.postValue("O preço informado é inválido")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            product.value!!.price = BigDecimal(product.value!!.price).setScale(2, RoundingMode.FLOOR).toString()
            val result = ProductRepository().addProduct(product.value!!)
            if(result is Result.Success) {
                _info.postValue("Produto adicionado com sucesso")
                _navigateBack.postValue(true)
            }else {
                _error.postValue("Erro ao adicionar produto!")
            }

            _showProgressBar.postValue(false)
        }
    }
}

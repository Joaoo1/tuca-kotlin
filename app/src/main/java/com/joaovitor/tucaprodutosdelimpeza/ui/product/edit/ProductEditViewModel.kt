package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

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

class ProductEditViewModel(private var mProduct: Product) : BaseViewModel() {

    var product = MutableLiveData(mProduct.copy())

    private var _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    private var _openDialogDelete = MutableLiveData(false)
    val openDialogDelete: LiveData<Boolean>
        get() = _openDialogDelete

    fun onClickSaveEditCadaster() {
        if(product.value!!.name.isEmpty() || product.value!!.price.isEmpty()) {
            _error.postValue("Os campos não podem ficar em branco")
            return
        }

        if(product.value!!.price.last() == '.' ) {
            _error.postValue("O preço informado é inválido")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            product.value!!.price = BigDecimal(product.value!!.price)
                .setScale(2, RoundingMode.FLOOR).toString()

            val result = ProductRepository().editProduct(product.value!!)

            if(result is Result.Success) {
                _info.postValue("Produto editado com sucesso")
                mProduct.bind(product.value!!)
            }else {
                _error.postValue("Erro ao editar produto!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickSaveEditStock() {
        if(product.value!!.manageStock && product.value!!.stock == 0) {
            _error.postValue("O estoque inicial não pode ser zero")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = ProductRepository().editProduct(product.value!!)

            if(result is Result.Success) {
                _info.postValue("Estoque salvo com sucesso")
                mProduct.bind(product.value!!)
            }else {
                _error.postValue("Erro ao salvar estoque!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickDeleteProduct() {
        _openDialogDelete.value = true
    }

    fun onClickDeleteProductPositive() {
        deleteProduct()
    }

    private fun deleteProduct() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = ProductRepository().deleteProduct(productId = product.value!!.id)
            if(result is Result.Success) {
                _info.postValue("Produto excluído com sucesso")
                _navigateBack.postValue(true)
            }else {
                _info.postValue("Erro ao excluir produto")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun setManageStock(checked: Boolean) {
        product.value?.manageStock = checked
    }

    fun doneNavigating() {
        _navigateBack.value = false
        _openDialogDelete.value = false
    }

}

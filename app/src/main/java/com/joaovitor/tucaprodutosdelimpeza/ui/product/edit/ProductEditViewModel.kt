package com.joaovitor.tucaprodutosdelimpeza.ui.product.edit

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class ProductEditViewModel(private var mProduct: Product) : ViewModel() {

    var product = MutableLiveData<Product>(mProduct.copy())

    fun onClickSaveEditCadaster() {
        if(product.value!!.name.isEmpty() || product.value!!.price.isEmpty()) {
            //TODO: Show error message: fields can't be empty
            return
        }

        if(product.value!!.price.last() == '.' ) {
            //TODO: Show a error message: value invalid
            return
        }

        GlobalScope.launch {
            product.value!!.price = BigDecimal(product.value!!.price)
                .setScale(2, RoundingMode.FLOOR).toString()

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

    fun setManageStock(checked: Boolean) {
        product.value?.manageStock = checked
    }

}

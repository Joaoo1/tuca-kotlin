package com.joaovitor.tucaprodutosdelimpeza.ui.product.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product

class ProductAddViewModel: ViewModel() {

    var product = MutableLiveData(Product())

    var isManagedStock = MutableLiveData<Boolean>(false)

}

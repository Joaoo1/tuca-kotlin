package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ProductRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockHistory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class StockHistoryViewModel : ViewModel() {

    private var emptyList: List<StockHistory> = emptyList()
    var stockHistories = MutableLiveData(emptyList)

    init {
        GlobalScope.launch {
            // stockHistories.postValue(productRepository.getStockHistories(productId))
        }
    }
}

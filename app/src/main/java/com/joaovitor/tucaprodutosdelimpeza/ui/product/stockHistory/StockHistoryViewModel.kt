package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockHistory
import java.util.*

class StockHistoryViewModel : ViewModel() {

    var emptyList: List<StockHistory> = emptyList()
    var stockHistories = MutableLiveData(emptyList)

    fun setStockHistories() {
        val myStockHistories = mutableListOf<StockHistory>()
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))
        myStockHistories.add(StockHistory(3400, "Joaquim", Date()))

        stockHistories.postValue(myStockHistories)
    }
}

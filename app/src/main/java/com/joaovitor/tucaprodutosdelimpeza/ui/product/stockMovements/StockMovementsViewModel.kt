package com.joaovitor.tucaprodutosdelimpeza.ui.product.stockMovements

import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StockRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockMovement
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StockMovementsViewModel(private val productId: String) : BaseViewModel() {

    var stockHistories:MutableLiveData<List<StockMovement>?> = MutableLiveData(listOf())

    init {
        GlobalScope.launch {
            val result = StockRepository().getStockMovements(productId)
            if (result is Result.Success) {
                stockHistories.postValue(result.data)
            } else {
                _error.postValue("Erro ao carregar movimentos de estoque")
            }
        }
    }
}

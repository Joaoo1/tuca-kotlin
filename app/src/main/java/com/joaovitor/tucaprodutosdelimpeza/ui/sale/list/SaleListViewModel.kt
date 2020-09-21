package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SaleListViewModel : ViewModel() {

    private var emptyList: List<Sale> = emptyList()
    var sales = MutableLiveData(emptyList)

    private var saleRepository: SaleRepository = SaleRepository()

    init { fetchSales() }

    private fun fetchSales(){
        GlobalScope.launch {
            sales.postValue(saleRepository.getSales())
        }
    }

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd


    private var _navigateToInfo = MutableLiveData<Sale>()
    val navigateToInfo: LiveData<Sale>
        get() = _navigateToInfo


    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfo.value = sale
    }

    //Filters
    private var _closeDialog = MutableLiveData<Boolean>(false)
    val closeDialog: LiveData<Boolean>
        get() = _closeDialog

    fun doneDialogClosing() {
        _closeDialog.value = false

        if(!isFiltered.value!!) {
            cleanFilters()
        }
    }

    private var _isFiltered = MutableLiveData<Boolean>(false)
    val isFiltered: LiveData<Boolean>
        get() = _isFiltered

    private var _startDate = MutableLiveData<Date?>(null)
    val startDate: LiveData<Date?>
        get() = _startDate

    private var _endDate = MutableLiveData<Date?>(null)
    val endDate: LiveData<Date?>
        get() = _endDate

    val paid = MutableLiveData<Boolean>(false)

    val unpaid = MutableLiveData<Boolean>(false)

    fun selectedStartDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _startDate.postValue(selectedDate)
    }

    fun selectedEndDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _endDate.postValue(selectedDate)
    }

    fun filterSalesList(){
        _isFiltered.value = true
        _closeDialog.value = true
        return
    }

    fun cleanFilters(){
        paid.value = false
        unpaid.value = false
        _startDate.value = null
        _endDate.value = null
        _isFiltered.value = false
        fetchSales()
    }

    fun refreshSalesList() {
        fetchSales()
    }
}

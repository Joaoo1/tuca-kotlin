package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SaleListViewModel : ViewModel() {

    private var saleRepository: SaleRepository = SaleRepository()

    var sales:MutableLiveData<List<Sale>> = MutableLiveData(emptyList())

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToInfo = MutableLiveData<Sale>()
    val navigateToInfo: LiveData<Sale>
        get() = _navigateToInfo

    //Filters
    private var isFiltered = false

    private var _closeDialog = MutableLiveData<Boolean>(false)
    val closeDialog: LiveData<Boolean>
        get() = _closeDialog

    private var _startDate = MutableLiveData<Date?>(null)
    val startDate: LiveData<Date?>
        get() = _startDate

    private var _endDate = MutableLiveData<Date?>(null)
    val endDate: LiveData<Date?>
        get() = _endDate

    val paid = MutableLiveData<Boolean>(false)
    val unpaid = MutableLiveData<Boolean>(false)

    init { fetchSales() }

    fun selectedStartDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _startDate.postValue(selectedDate)
    }

    fun selectedEndDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _endDate.postValue(selectedDate)
    }

    fun filterSalesList(){
        isFiltered = true
        val dateRange = getDateRange()
        val paidFilter: Boolean? = getPaidFilter()

        GlobalScope.launch {
            sales.postValue(saleRepository.getFilteredSales(dateRange, paidFilter))
        }

        doneDialogClosing()
        return
    }

    /** Validate the payments situation checkboxes*/
    private fun getPaidFilter(): Boolean? {
        if(paid.value!! == unpaid.value!!) {
            return null
        }

        return paid.value!!
    }

    /** Validate the date range fields */
    private fun getDateRange() : DateRange? {
        if(_startDate.value == _endDate.value){
            _startDate.value?.let {
                return DateRange(it,  _endDate.value!!)
            }

            return null
        }

        /** Check if one of the two dates was not informed */
        if(_startDate.value == null || _endDate.value == null) {
            //TODO: Show message error: Select start and end date
            return null
        }

        /** The given start date can't be greater than the end date */
        if(_startDate.value!!.compareTo(_endDate.value!!) == 1) {
            //TODO: Show message error: Start date is greater than end date
            return null
        }

        return DateRange(_startDate.value!!, _endDate.value!!)
    }

    fun cleanFilters(){
        paid.value = false
        unpaid.value = false
        _startDate.value = null
        _endDate.value = null
        isFiltered = false
        fetchSales()
    }

    private fun fetchSales(){
        GlobalScope.launch {
            sales.postValue(saleRepository.getSales())
        }
    }

    fun onClickRefreshList() {
        fetchSales()
    }

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfo.value = sale
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }

    fun doneDialogClosing() {
        _closeDialog.value = false

        if(!isFiltered) {
            cleanFilters()
        }
    }
}

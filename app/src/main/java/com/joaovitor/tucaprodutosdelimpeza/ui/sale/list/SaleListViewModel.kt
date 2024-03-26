package com.joaovitor.tucaprodutosdelimpeza.ui.sale.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SaleListViewModel : BaseViewModel() {

    private var saleRepository: SaleRepository = SaleRepository()

    private var _sales:MutableLiveData<List<Sale>?> = MutableLiveData(listOf())

    var sales:MutableLiveData<List<Sale>?> = MutableLiveData(listOf())

    private var _filteredSales: MutableLiveData<List<Sale>> = MutableLiveData(emptyList())
    val filteredSales: LiveData<List<Sale>>
        get() = _filteredSales

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToInfo = MutableLiveData<Sale?>()
    val navigateToInfo: LiveData<Sale?>
        get() = _navigateToInfo

    //Filters
    private var isFiltered = false

    private var _closeFiltersDialog = MutableLiveData(false)
    val closeFiltersDialog: LiveData<Boolean>
        get() = _closeFiltersDialog

    private var _startDate = MutableLiveData<Date?>(null)
    val startDate: LiveData<Date?>
        get() = _startDate

    private var _endDate = MutableLiveData<Date?>(null)
    val endDate: LiveData<Date?>
        get() = _endDate

    val paid = MutableLiveData(false)
    val unpaid = MutableLiveData(false)

    init { fetchSales() }

    fun selectedStartDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _startDate.postValue(selectedDate)
    }

    fun selectedEndDate(millis: Long) {
        val selectedDate = Date(FormatDate.add3HoursToTimestamp(millis))
        _endDate.postValue(selectedDate)
    }

    private fun filterSalesList(){
        isFiltered = true
        val dateRange = getDateRange()
        val paidFilter: Boolean? = getPaidFilter()

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultSales = saleRepository.getFilteredSales(dateRange, paidFilter)
            if (resultSales is Result.Success) {
                sales.postValue(resultSales.data)
            } else {
                _error.postValue("Erro ao carregar vendas filtradas!")
            }

            _closeFiltersDialog.postValue( true)

            _showProgressBar.postValue(false)

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
            _error.postValue("Selecione o período de tempo")
            return null
        }

        /** The given start date can't be greater than the end date */
        if(_startDate.value!!.compareTo(_endDate.value!!) == 1) {
            _error.postValue("Data inicial não pode ser maior que data final")
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
            _showProgressBar.postValue(true)

            val resultSales = saleRepository.getSales()
            if (resultSales is Result.Success) {
               sales.postValue(resultSales.data)
               _sales.postValue(resultSales.data)
            } else {
                _error.postValue((resultSales as Result.Error).exception.toString())
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickRefreshList() {
        fetchSales()
    }

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onClickFilterButton() {
        filterSalesList()
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfo.value = sale
    }

    fun doneNavigating(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }

    fun doneDialogClosing() {
        _closeFiltersDialog.value = false

        if(!isFiltered) {
            cleanFilters()
        }
    }

    fun filterSales(query: String) {
        if(query.isEmpty()) {
            sales.postValue(_sales.value!!)
            _showProgressBar.postValue(false)
        } else {
            _showProgressBar.postValue(true)

            GlobalScope.launch {
                val result = SaleRepository().getSalesById(Integer.parseInt(query))
                if(result is Result.Success) {
                    sales.postValue(result.data)
                }
                _showProgressBar.postValue(false)
            }
        }

    }
}

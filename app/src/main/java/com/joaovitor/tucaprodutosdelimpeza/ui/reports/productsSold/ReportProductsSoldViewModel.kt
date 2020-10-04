package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.ReportRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSold
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class ReportProductsSoldViewModel : BaseViewModel() {

    private var _productsSoldList = MutableLiveData<List<ProductSold>>()
    val productsSoldList: LiveData<List<ProductSold>>
        get() = _productsSoldList

    private var _navigateToProductsSoldList = MutableLiveData<Boolean>()
    val navigateToProductsSoldList: LiveData<Boolean>
        get() = _navigateToProductsSoldList

    var startDate = MutableLiveData<Date>(null)
    var endDate = MutableLiveData<Date>(null)

    private var _openStartDatePicker = MutableLiveData<Boolean>()
    val openStartDatePicker: LiveData<Boolean>
        get() = _openStartDatePicker

    private var _openEndDatePicker = MutableLiveData<Boolean>()
    val openEndDatePicker: LiveData<Boolean>
        get() = _openEndDatePicker

    private fun generateReport() {
        if(validateFields()) {
            GlobalScope.launch {
                _showProgressBar.postValue(true)

                val result = ReportRepository().generateProductsSoldReport(
                    DateRange(startDate.value!!, endDate.value!!))

                if (result is Result.Success) {
                    _navigateToProductsSoldList.postValue(true)
                    _productsSoldList.postValue(result.data)
                } else {
                    _error.postValue("Ocorreu um erro ao gerar relatório!")
                }

                _showProgressBar.postValue(false)
            }
        }
    }

    private fun validateFields() : Boolean {
        if(startDate.value == null || endDate.value == null) {
            _error.postValue("Selecione o período de tempo")
            return false
        }
        if(startDate.value!! != endDate.value!!
            && startDate.value!!.compareTo(endDate.value!!) == 1) {
            _error.postValue("Data inicial não pode ser maior que data final")
            return false
        }

        return true
    }

    fun openStartDatePicker(){
        _openStartDatePicker.value = true
    }

    fun openEndDatePicker(){
        _openEndDatePicker.value = true
    }

    fun onSelectStartDate(millis: Long) {
        val date = FormatDate.add3HoursToTimestamp(millis)
        startDate.postValue(Date(date))
        _openStartDatePicker.value = false
    }

    fun onSelectEndDate(millis: Long) {
        val date = FormatDate.add3HoursToTimestamp(millis)
        endDate.postValue(Date(date))
        _openEndDatePicker.value = false
    }

    fun onClickFilterButton() {
       generateReport()
    }

    fun doneNavigating(){
        _navigateToProductsSoldList.value = false
    }
}

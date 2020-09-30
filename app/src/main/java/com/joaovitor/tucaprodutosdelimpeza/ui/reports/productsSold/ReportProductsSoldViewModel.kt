package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ReportRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSold
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class ReportProductsSoldViewModel : ViewModel() {

    var productsSoldList = MutableLiveData<List<ProductSold>>()

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
            //TODO: Progress bar
            GlobalScope.launch {
                val result = ReportRepository().generateProductsSoldReport(
                    DateRange(startDate.value!!, endDate.value!!))

                if (result is Result.Success) {
                    _navigateToProductsSoldList.postValue(true)
                    productsSoldList.postValue(result.data)
                } else {
                    //TODO: Show error message
                    return@launch
                }
            }
        }
    }

    private fun validateFields() : Boolean {
        if(startDate.value == null || endDate.value == null) {
            //TODO: Show error message: select the date range
            return false
        }
        if(startDate.value!! != endDate.value!!
            && startDate.value!!.compareTo(endDate.value!!) == 1) {
            //TODO: Show error message: start date is greater than end date
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

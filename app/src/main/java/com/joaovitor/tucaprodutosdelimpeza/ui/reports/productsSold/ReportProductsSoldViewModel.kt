package com.joaovitor.tucaprodutosdelimpeza.ui.reports.productsSold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.android.synthetic.main.fragment_report_sales.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ReportProductsSoldViewModel : ViewModel() {

    private var _navigateToProductsSoldList = MutableLiveData<List<ProductSale>>()
    val navigateToProductsSoldList: LiveData<List<ProductSale>>
        get() = _navigateToProductsSoldList

    var startDate = MutableLiveData<Date>(Date())
    var endDate = MutableLiveData<Date>(Date())

    private var _openStartDatePicker = MutableLiveData<Boolean>()
    val openStartDatePicker: LiveData<Boolean>
        get() = _openStartDatePicker

    private var _openEndDatePicker = MutableLiveData<Boolean>()
    val openEndDatePicker: LiveData<Boolean>
        get() = _openEndDatePicker

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
        _navigateToProductsSoldList.value = listOf()
    }

    fun doneNavigation(){
        _navigateToProductsSoldList.value = null
    }
}

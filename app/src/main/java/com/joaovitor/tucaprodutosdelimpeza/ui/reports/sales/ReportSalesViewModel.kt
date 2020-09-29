package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.android.synthetic.main.fragment_report_sales.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ReportSalesViewModel : ViewModel() {

    private val address = MutableLiveData<String>()
    var startDate = MutableLiveData<Date>(Date())
    var endDate = MutableLiveData<Date>(Date())

    var addressRadioChecked = MutableLiveData<Int>(R.id.radio_button_no_filter)
    var paymentRadioChecked = MutableLiveData<Int>(R.id.radio_button_all)

    private var _openDialog = MutableLiveData<Boolean>()
    val openDialog: LiveData<Boolean>
        get() = _openDialog

    private var _openSelectAddressDialog = MutableLiveData<Array<String>>()
    val openSelectAddressDialog: LiveData<Array<String>>
        get() = _openSelectAddressDialog

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
        if(addressRadioChecked.value == R.id.radio_button_no_filter) {
            _openDialog.value = true
        }else {
            openSelectAddressDialog()
        }
    }

    private fun openSelectAddressDialog() {
        GlobalScope.launch {
            val address: List<String> = when(addressRadioChecked.value){
                R.id.radio_button_street ->  StreetRepository().getStreets().map { it.name }
                R.id.radio_button_neighborhood ->  NeighborhoodRepository().getNeighborhoods().map { it.name }
                R.id.radio_button_city ->  CityRepository().getCities().map { it.name }
                else -> listOf()
            }

            _openSelectAddressDialog.postValue(address.toTypedArray())
        }
    }

    fun onSelectAddress(name: String){
        address.value = name
        _openSelectAddressDialog.value = null
        _openDialog.value = true
    }

    fun doneNavigation(){
        _openDialog.value = false
    }
}

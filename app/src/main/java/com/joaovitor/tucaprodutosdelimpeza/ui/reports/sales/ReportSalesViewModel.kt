package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.android.synthetic.main.fragment_report_sales.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ReportSalesViewModel : ViewModel() {

    val filteredSales = MutableLiveData<List<Sale>>(listOf())

    private val address = MutableLiveData<Address>()
    var startDate = MutableLiveData<Date>()
    var endDate = MutableLiveData<Date>()

    var addressRadioChecked = MutableLiveData<Int>(R.id.radio_button_no_filter)
    var paymentRadioChecked = MutableLiveData<Int>(R.id.radio_button_all)

    private var _openDialog = MutableLiveData<Boolean>()
    val openDialog: LiveData<Boolean>
        get() = _openDialog

    private var _openSelectAddressDialog = MutableLiveData<List<Address>>()
    val openSelectAddressDialog: LiveData<List<Address>>
        get() = _openSelectAddressDialog

    private var _openStartDatePicker = MutableLiveData<Boolean>()
    val openStartDatePicker: LiveData<Boolean>
        get() = _openStartDatePicker

    private var _openEndDatePicker = MutableLiveData<Boolean>()
    val openEndDatePicker: LiveData<Boolean>
        get() = _openEndDatePicker

    private var _navigateToInfoSale = MutableLiveData<Sale?>()
    val navigateToInfoSale: LiveData<Sale?>
        get() = _navigateToInfoSale

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
            filterSales()
        }else {
            openSelectAddressDialog()
        }
    }

    fun onSelectAddress(selectedAddress: Address){
        address.value = selectedAddress
        _openSelectAddressDialog.value = null
        filterSales()
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfoSale.value = sale
    }

    fun doneNavigating(){
        _openDialog.value = false
        _navigateToInfoSale.value = null
    }

    private fun openSelectAddressDialog() {
        GlobalScope.launch {
            val address: List<Address> = when(addressRadioChecked.value){
                R.id.radio_button_street -> StreetRepository().getStreets()
                R.id.radio_button_neighborhood -> NeighborhoodRepository().getNeighborhoods()
                R.id.radio_button_city -> CityRepository().getCities()
                else -> listOf()
            }

            _openSelectAddressDialog.postValue(address)
        }
    }

    private fun filterSales() {

        /** Check if one of the two dates was not informed */
        if(startDate.value == null || endDate.value == null) {
            //TODO: Show message error: Select start and end date
            return
        }

        /** The given start date can't be greater than the end date */
        if(startDate.value!!.compareTo(endDate.value!!) == 1) {
            //TODO: Show message error: Start date is greater than end date
            return
        }

        val paid = when(paymentRadioChecked.value) {
            R.id.radio_button_all -> null
            R.id.radio_button_paid -> true
            R.id.radio_button_unpaid -> false
            else -> null
        }

        GlobalScope.launch {
            val sales = SaleRepository()
                .getFilteredSales(DateRange(startDate.value!!, endDate.value!!), paid, address.value)

            filteredSales.postValue(sales)
            _openDialog.postValue(true)
        }
    }
}

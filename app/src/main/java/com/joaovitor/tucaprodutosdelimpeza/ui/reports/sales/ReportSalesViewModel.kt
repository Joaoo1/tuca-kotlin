package com.joaovitor.tucaprodutosdelimpeza.ui.reports.sales

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.R
import com.joaovitor.tucaprodutosdelimpeza.bluetooth.Bluetooth
import com.joaovitor.tucaprodutosdelimpeza.bluetooth.PrinterFunctions
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Date

class ReportSalesViewModel : BaseViewModel() {

    val filteredSales = MutableLiveData<List<Sale>?>(listOf())

    private val address = MutableLiveData<Address>()
    var startDate = MutableLiveData<Date>()
    var endDate = MutableLiveData<Date>()

    var addressRadioChecked = MutableLiveData(R.id.radio_button_no_filter)
    var paymentRadioChecked = MutableLiveData(R.id.radio_button_all)

    private var _openDialog = MutableLiveData<Boolean>()
    val openDialog: LiveData<Boolean>
        get() = _openDialog

    private var _openSelectAddressDialog = MutableLiveData<List<Address>?>()
    val openSelectAddressDialog: MutableLiveData<List<Address>?>
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

    private var _requestBluetoothOn = MutableLiveData(false)
    val requestBluetoothOn: LiveData<Boolean>
        get() = _requestBluetoothOn

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
            _showProgressBar.postValue(true)
            @Suppress("ThrowableNotThrown")
            val result: Result<List<Address>> = when(addressRadioChecked.value){
                R.id.radio_button_street -> StreetRepository().getStreets()
                R.id.radio_button_neighborhood -> NeighborhoodRepository().getNeighborhoods()
                R.id.radio_button_city -> CityRepository().getCities()
                else -> Result.Error(Exception())
            }

            if(result is Result.Success) {
                _openSelectAddressDialog.postValue(result.data)
            } else {
                _error.postValue("Ocorreu um erro ao carregar endereços")
            }

            _showProgressBar.postValue(false)
        }
    }

    private fun filterSales() {
        /** Check if one of the two dates was not informed */
        if(startDate.value == null || endDate.value == null) {
            _error.postValue("Selecione o período de tempo")
            return
        }

        /** The given start date can't be greater than the end date */
        if(startDate.value!!.compareTo(endDate.value!!) == 1) {
            _error.postValue("Data inicial não pode ser maior que data final")
            return
        }

        val paid = when(paymentRadioChecked.value) {
            R.id.radio_button_all -> null
            R.id.radio_button_paid -> true
            R.id.radio_button_unpaid -> false
            else -> null
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)
            val resultSales = SaleRepository()
                .getFilteredSales(DateRange(startDate.value!!, endDate.value!!), paid, address.value)

            if(resultSales is Result.Success) {
                filteredSales.postValue(resultSales.data)
                _openDialog.postValue(true)
            } else {
                _error.postValue("Ocorreu um erro ao carregar vendas")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickPrintReport(context: Context) {
        printReport(context)
    }

    /* Printer */
    private fun printReport(context: Context) {
        val printerFunctions = PrinterFunctions(context)
        val hasPermission = Bluetooth().checkPermission(context)

        if (hasPermission) {
            printerFunctions.printSalesList(filteredSales.value!!)
        } else {
            _requestBluetoothOn.value = true
        }
    }

    fun onBluetoothResult(resultCode: Int, context: Context) {
        if(resultCode == Activity.RESULT_OK) {
            printReport(context)
        }else if (resultCode == Activity.RESULT_CANCELED) {
            _error.value = "Ocorreu um erro ao ativar bluetooth!"
        }
    }
}

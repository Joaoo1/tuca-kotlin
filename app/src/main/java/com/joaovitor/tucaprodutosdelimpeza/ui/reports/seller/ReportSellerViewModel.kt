package com.joaovitor.tucaprodutosdelimpeza.ui.reports.seller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.UserRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.data.model.User
import com.joaovitor.tucaprodutosdelimpeza.data.util.DateRange
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.max

class ReportSellerViewModel : BaseViewModel() {
    val startDate = MutableLiveData<Date?>()
    val endDate = MutableLiveData<Date?>()
    var selectedSeller = MutableLiveData<User?>()
    val filteredSales = MutableLiveData<List<Sale>?>(listOf())

    private var _sellers = MutableLiveData<List<User>>(emptyList())
    val sellers: MutableLiveData<List<User>>
        get() = _sellers

    private var _openStartDatePicker = MutableLiveData<Boolean>()
    val openStartDatePicker: LiveData<Boolean>
        get() = _openStartDatePicker

    private var _openEndDatePicker = MutableLiveData<Boolean>()
    val openEndDatePicker: LiveData<Boolean>
        get() = _openEndDatePicker

    private var _navigateToFilteredSales = MutableLiveData<Boolean>()
    val navigateToFilteredSales: LiveData<Boolean>
        get() = _navigateToFilteredSales

    private var _navigateToInfoSale = MutableLiveData<Sale?>()
    val navigateToInfoSale: LiveData<Sale?>
        get() = _navigateToInfoSale

    init {
        viewModelScope.launch {
            val userRepository = UserRepository()
            val resultSellers = userRepository.getSellers()
            if(resultSellers is Result.Success) {
               _sellers.postValue(resultSellers.data ?: listOf())
            } else {
                _error.postValue("Erro ao carregar vendedores!")
            }
        }
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
        /** The given start date can't be greater than the end date */
        if(startDate.value!!.compareTo(endDate.value!!) == 1) {
            _error.postValue("Data inicial não pode ser maior que data final")
            return
        }

        viewModelScope.launch {
            _showProgressBar.postValue(true)
            val resultSales = SaleRepository()
                .getSalesBySeller(selectedSeller.value?.uid ?: "", DateRange(startDate.value!!, endDate.value!!))

            if(resultSales is Result.Success) {
                if(resultSales.data?.isEmpty() == true){
                    _info.postValue("Nenhuma venda encontrada")
                } else {
                    filteredSales.postValue(resultSales.data)
                    _navigateToFilteredSales.postValue(true)
                }
            } else {
                _error.postValue("Ocorreu um erro ao carregar vendas")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onSelectSeller(position: Int) {
        val index = max(position - 1, 0)
        selectedSeller.postValue(sellers.value?.get(index))
    }

    fun onSaleClicked(sale: Sale) {
        _navigateToInfoSale.value = sale
    }

    fun doneNavigating(){
        _navigateToInfoSale.value = null
        _navigateToFilteredSales.value = false
        startDate.postValue(null)
        endDate.postValue(null)
        selectedSeller.postValue(null)
    }
}

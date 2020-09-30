package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SaleInfoViewModel : ViewModel() {

    private val saleRepository = SaleRepository()

    private var _sale =  MutableLiveData<Sale>()
    val sale: LiveData<Sale>
        get() = _sale

    private var _openPaymentDialog = MutableLiveData<Boolean>()
    val openPaymentDialog: LiveData<Boolean>
        get() = _openPaymentDialog

    private var _openDeleteDialog = MutableLiveData<Boolean>()
    val openDeleteDialog: LiveData<Boolean>
        get() = _openDeleteDialog

    private var _navigateToEditProducts = MutableLiveData<Boolean>()
    val navigateToEditProducts: LiveData<Boolean>
        get() = _navigateToEditProducts

    private var _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    /* Database functions */
    fun deleteSale() {
        GlobalScope.launch {
            sale.value?.id?.let {
                val result = saleRepository.deleteSale(it)
                if (result is Result.Success) {
                    //TODO: Show a success message
                    _navigateBack.postValue(true)
                } else {
                    //TODO: Show a error message
                    return@launch
                }
            }
        }
    }

    /** Register a payment based on the given value in [value] */
    fun registerPayment(value: String) {
        val mSale = sale.value!!

        /**
         * Checking if string is empty
         * if its true, finish sale and set it as paid
         */
        if(value.isEmpty()) {
            mSale.finishSale()
            return
        }
        /**
         * Check if the informed value last letter is not a dot
         * A value with dot, crash the app when try to convert it to BigDecimal
         */
        if(value.last() == '.') {
            //TODO: Show a message error: Invalid value
            return
        }

        when(BigDecimal(value).compareTo(BigDecimal(mSale.toReceive))){
            /**
             *  Value informed by user is less than value to receive
             *  So just register a payment on sale
             */
            -1 -> mSale.registerPayment(value)

            /**
             *  Value informed by user is equals to value to receive
             *  So finish sale and set it as paid
             */
            0 -> mSale.finishSale()

            /**
             * Value informed is greater than value to receive
             * Show a error message to user
             */
            1 -> {
                /*TODO: Show a error: Informed value is greater than value to receive*/
                return
            }
        }


        // Sale properly set, so save it into firestore
        GlobalScope.launch {
            saleRepository.editSale(mSale)
            //TODO: show a message to user based on Result of editSale()

            _sale.postValue(mSale)
            _openPaymentDialog.postValue(false)
        }
    }

    /* User actions */
    fun onClickEditProducts() {
        /** Can't edit the products of a sale that is paid */
        if(_sale.value?.paid!!){
            //TODO: Show a error: Can't edit the products of a sale that is paid
            return
        }

        _navigateToEditProducts.value = true
    }

    fun onClickDeleteSale() {
        _openDeleteDialog.value = true
    }

    fun onClickRegisterPayment() {
        if(!sale.value?.paid!!) _openPaymentDialog.value = true
    }

    fun doneNavigating(){
        _navigateToEditProducts.value = false
        _navigateBack.value = false
        _openDeleteDialog.value = false
        _openPaymentDialog.value = false
    }

    fun setSale(sale: Sale) {
        _sale.value = sale
    }
}

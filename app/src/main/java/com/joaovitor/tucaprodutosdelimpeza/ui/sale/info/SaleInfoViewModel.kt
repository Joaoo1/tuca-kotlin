package com.joaovitor.tucaprodutosdelimpeza.ui.sale.info

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.bluetooth.PrinterFunctions
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SaleInfoViewModel : BaseViewModel() {

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

    private var _requestBluetoothOn = MutableLiveData<Boolean>()
    val requestBluetoothOn: LiveData<Boolean>
        get() = _requestBluetoothOn

    /* Database functions */
    fun deleteSale() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            sale.value?.id?.let {
                val result = saleRepository.deleteSale(sale.value!!)
                if (result is Result.Success) {
                    _info.postValue("Venda excluída com sucesso")
                    _navigateBack.postValue(true)
                } else {
                    _error.postValue("Erro ao excluir venda!")
                }
            }

            _showProgressBar.postValue(false)
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
        } else {
            /**
             * Check if the informed value last letter is not a dot
             * A value with dot, crash the app when try to convert it to BigDecimal
             */
            if(value.last() == '.') {
                _error.postValue("O valor informado é inválido")
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
                    _error.postValue("Valor pago é maior que o total da venda")
                    return
                }
            }
        }

        // Sale properly set, so save it into firestore
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = saleRepository.editSale(mSale)

            if (result is Result.Success) {
                _info.postValue("Venda editado com sucesso")
            } else {
                _error.postValue("Erro ao editar venda")
            }

            _sale.postValue(mSale)
            _openPaymentDialog.postValue(false)

            _showProgressBar.postValue(false)
        }
    }

    /* User actions */
    fun onClickEditProducts() {
        /** Can't edit the products of a sale that is paid */
        if(_sale.value?.paid!!){
            _error.postValue("Não é possível alterar produtos de uma venda paga!")
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

    fun onClickPrintReceipt(context: Context) {
        printReceipt(context)
    }

    fun doneNavigating(){
        _navigateToEditProducts.value = false
        _navigateBack.value = false
        _openDeleteDialog.value = false
        _openPaymentDialog.value = false
        _requestBluetoothOn.value = false
    }

    fun setSale(sale: Sale) {
        _sale.value = sale
    }

    /* Printer */
    private fun printReceipt(context: Context) {
        val printerFunctions = PrinterFunctions(context)

        if (printerFunctions.btAdapter != null && printerFunctions.btAdapter.isEnabled) {
            printerFunctions.printReceipt(_sale.value!!, _sale.value!!.products)
        } else {
            _requestBluetoothOn.value = true
        }
    }

    fun onBluetoothResult(resultCode: Int, context: Context) {
        if(resultCode == Activity.RESULT_OK) {
            printReceipt(context)
        }else if (resultCode == Activity.RESULT_CANCELED) {
            _error.value = "Ocorreu um erro ao ativar bluetooth!"
        }
    }
}

package com.joaovitor.tucaprodutosdelimpeza.ui.client.info

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import com.joaovitor.tucaprodutosdelimpeza.util.Phone
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientInfoViewModel(mClient: Client) : BaseViewModel() {

    var client = MutableLiveData(mClient)
    var clientSales:MutableLiveData<List<Sale>> = MutableLiveData(emptyList())

    var notFoundSales = MutableLiveData(false)

    private var _navigateToEditClient = MutableLiveData<Client?>()
    val navigateToEditClient: LiveData<Client?>
        get() = _navigateToEditClient

    private var _navigateToInfoSale = MutableLiveData<Sale?>()
    val navigateToInfoSale: LiveData<Sale?>
        get() = _navigateToInfoSale

    private var _callClientPhone = MutableLiveData<Intent?>()
    val callClientPhone: LiveData<Intent?>
        get() = _callClientPhone

    private var _sendWhatsappClient = MutableLiveData<Intent?>()
    val sendWhatsappClient: LiveData<Intent?>
        get() = _sendWhatsappClient

    private var _requestCallPermission = MutableLiveData(false)
    val requestCallPermission: LiveData<Boolean>
        get() = _requestCallPermission

    init {
        fetchClientSales()
    }

    private fun fetchClientSales() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultClientSales = SaleRepository().getSalesByClient(client.value!!.id)
            if (resultClientSales is Result.Success) {
                val data = resultClientSales.data!!
                clientSales.postValue(data)
                if(data.isEmpty()) notFoundSales.postValue(true)
            } else {
                _error.postValue("Erro ao buscar vendas do cliente")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickEditClient() {
        _navigateToEditClient.value = client.value
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onClickFinishSale(sale: Sale) {
        if(sale.paid){
            _info.postValue("Está venda já está paga")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            sale.finishSale()
            val result = SaleRepository().editSale(sale)
            if (result is Result.Success){
                _info.postValue("Venda finalizada com sucesso")
                fetchClientSales()
            }else {
                _error.postValue("Erro ao buscar vendas do cliente")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickInfoSale(sale: Sale) {
        _navigateToInfoSale.value = sale
    }

    fun onClickWhatsapp() {
        sendWhatsappClient()
    }

    fun doneNavigating(){
        _navigateToEditClient.value = null
        _navigateToInfoSale.value = null
    }

    fun onClickCallClient(context: Context) {
        if(isPermissionGrantedForCalls(context)) {
            callClientPhone()
        } else {
            _requestCallPermission.value = true
        }
    }

    /* Whatsapp functions */
    private fun sendWhatsappClient() {
        client.value?.let{
            if(it.phone.isEmpty()) {
                _error.postValue("Cliente não possui um número cadastrado")
                return
            }

            val clientPhoneNumber = it.phone.filter { it1 -> it1.isDigit()}
            val phoneNumber = Phone.getFormattedNumberToWhatsapp(clientPhoneNumber)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            _sendWhatsappClient.value = intent
        }
    }

    fun doneSendWhatsapp() {
        _sendWhatsappClient.value = null
    }

    /* Call phone functions */
    fun onRequestCallPermissionResult(grantResults: IntArray) {
        if(grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            callClientPhone()
        }else {
            _error.value = "Permissão para realizar ligações foi negada pelo usuário!"
        }
    }

    private fun isPermissionGrantedForCalls(context: Context): Boolean {
        val callPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CALL_PHONE
        )

        return callPermission == PERMISSION_GRANTED
    }

    private fun callClientPhone() {
        val intent = Intent(Intent.ACTION_CALL)
        val uri: String = client.value!!.phone.filter {it.isDigit()}
        intent.data = Uri.parse("tel:$uri")
        _callClientPhone.value = intent
    }

    fun doneCalling() {
        _callClientPhone.value = null
    }
}

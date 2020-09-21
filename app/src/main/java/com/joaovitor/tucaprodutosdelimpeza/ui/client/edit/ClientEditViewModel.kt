package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientEditViewModel : ViewModel() {

    var clients = MutableLiveData<List<Client>>(emptyList())

    private fun fetchAddresses(){
        GlobalScope.launch {
            clients.postValue(ClientRepository().getClients())
        }
    }

    init {
        fetchAddresses()
    }

    //navigation
    private var _navigateToManageAddress = MutableLiveData<Boolean>()
    val navigateToManageAddress: LiveData<Boolean>
        get() = _navigateToManageAddress

    fun doneNavigation(){
        _navigateToManageAddress.value = false
    }
}

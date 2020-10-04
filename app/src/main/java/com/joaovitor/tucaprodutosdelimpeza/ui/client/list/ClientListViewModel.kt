package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.SaleRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientListViewModel : BaseViewModel() {

    var clients = MutableLiveData<List<Client>>(emptyList())

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToInfo = MutableLiveData<Client>()
    val navigateToInfo: LiveData<Client>
        get() = _navigateToInfo

    init {
       fetchClients()
    }

    private fun fetchClients() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)
            val resultClient = ClientRepository().getClients()
            if (resultClient is Result.Success) {
                clients.postValue(resultClient.data)
            } else {
                _error.postValue("Erro ao carregar clientes")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickRefreshList() {
        fetchClients()
    }

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onClientClicked(client: Client) {
        _navigateToInfo.value = client
    }

    fun doneNavigating(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }

}

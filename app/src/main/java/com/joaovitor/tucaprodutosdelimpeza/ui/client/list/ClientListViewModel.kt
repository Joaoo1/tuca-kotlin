package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

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

class ClientListViewModel : ViewModel() {

    var clients = MutableLiveData<List<Client>>(emptyList())

    init {
        GlobalScope.launch {
            clients.postValue(ClientRepository().getClients())
        }
    }

    //navigation
    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToInfo = MutableLiveData<Client>()
    val navigateToInfo: LiveData<Client>
        get() = _navigateToInfo

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onClientClicked(client: Client) {
        _navigateToInfo.value = client
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToInfo.value = null
    }
}

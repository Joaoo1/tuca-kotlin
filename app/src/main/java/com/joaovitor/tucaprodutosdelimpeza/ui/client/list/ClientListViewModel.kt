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
    // var clientSales = MutableLiveData<List<Sale>>(emptyList())

    private fun fetchClients(){
        GlobalScope.launch {
            clients.postValue(ClientRepository().getClients())
        }
    }

    init {
        fetchClients()
    }

    //navigation
    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    private var _navigateToEdit = MutableLiveData<Client>()
    val navigateToEdit: LiveData<Client>
        get() = _navigateToEdit

    private var _openInfoDialog = MutableLiveData<Client>()
    val openInfoDialog: LiveData<Client>
        get() = _openInfoDialog

    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun onClientClicked(client: Client) {
        _openInfoDialog.value = client
    }

    fun openEditClient(client: Client) {
        _navigateToEdit.value = client
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToEdit.value = null
        _openInfoDialog.value = null
    }
}

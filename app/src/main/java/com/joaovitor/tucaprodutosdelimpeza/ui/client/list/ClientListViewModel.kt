package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client

class ClientListViewModel : ViewModel() {

    var emptyList: List<Client> = emptyList()
    var clients = MutableLiveData(emptyList)

    fun setClients() {
        val myProducts = mutableListOf<Client>()
        myProducts.add(Client("Amanda", "Palhoça"))
        myProducts.add(Client("Pedro", "Palhoça"))
        myProducts.add(Client("Rosa", "Palhoça"))
        myProducts.add(Client("Joaquim", "Palhoça"))
        myProducts.add(Client("Vinicius", "Palhoça"))
        myProducts.add(Client("Alfredo", "Palhoça"))
        myProducts.add(Client("Marcos", "Palhoça"))
        myProducts.add(Client("Kauã", "Palhoça"))
        myProducts.add(Client("Amanda", "Palhoça"))

        clients.postValue(myProducts)
    }

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd


    private var _navigateToEdit = MutableLiveData<Client>()
    val navigateToEdit: LiveData<Client>
        get() = _navigateToEdit


    fun onClickFab(){
        _navigateToAdd.value = true
    }

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToEdit.value = null
    }

    fun onClientClicked(client: Client) {
        _navigateToEdit.value = client
    }

}

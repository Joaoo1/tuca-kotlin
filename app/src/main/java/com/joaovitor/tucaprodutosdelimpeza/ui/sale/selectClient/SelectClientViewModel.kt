package com.joaovitor.tucaprodutosdelimpeza.ui.sale.selectClient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client

class SelectClientViewModel : ViewModel() {

    private var _navigateToAdd = MutableLiveData<Boolean>()
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    var emptyList: List<Client> = emptyList()
    var clients = MutableLiveData(emptyList)

    fun setClients() {
        val myClients = mutableListOf<Client>()
        myClients.add(Client("a","Amanda", "Palhoça"))
        myClients.add(Client("a","Pedro", "Palhoça"))
        myClients.add(Client("a","Rosa", "Palhoça"))
        myClients.add(Client("a","Joaquim", "Palhoça"))
        myClients.add(Client("a","Vinicius", "Palhoça"))
        myClients.add(Client("a","Alfredo", "Palhoça"))
        myClients.add(Client("a","Marcos", "Palhoça"))
        myClients.add(Client("a","Kauã", "Palhoça"))
        myClients.add(Client("a","Amanda", "Palhoça"))
        myClients.add(Client("a","Pedro", "Palhoça"))
        myClients.add(Client("a","Rosa", "Palhoça"))
        myClients.add(Client("a","Joaquim", "Palhoça"))
        myClients.add(Client("a","Vinicius", "Palhoça"))
        myClients.add(Client("a","Alfredo", "Palhoça"))
        myClients.add(Client("a","Marcos", "Palhoça"))
        myClients.add(Client("a","Kauã", "Palhoça"))

        clients.postValue(myClients)
    }

    fun onClientClicked(client: Client){
        _navigateToAdd.value = true
    }

}

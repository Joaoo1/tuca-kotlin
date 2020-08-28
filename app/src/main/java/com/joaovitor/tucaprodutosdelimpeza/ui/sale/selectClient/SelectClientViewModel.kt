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
        myClients.add(Client("Amanda", "Palhoça"))
        myClients.add(Client("Pedro", "Palhoça"))
        myClients.add(Client("Rosa", "Palhoça"))
        myClients.add(Client("Joaquim", "Palhoça"))
        myClients.add(Client("Vinicius", "Palhoça"))
        myClients.add(Client("Alfredo", "Palhoça"))
        myClients.add(Client("Marcos", "Palhoça"))
        myClients.add(Client("Kauã", "Palhoça"))
        myClients.add(Client("Amanda", "Palhoça"))
        myClients.add(Client("Amanda", "Palhoça"))
        myClients.add(Client("Pedro", "Palhoça"))
        myClients.add(Client("Rosa", "Palhoça"))
        myClients.add(Client("Joaquim", "Palhoça"))
        myClients.add(Client("Vinicius", "Palhoça"))
        myClients.add(Client("Alfredo", "Palhoça"))
        myClients.add(Client("Marcos", "Palhoça"))
        myClients.add(Client("Kauã", "Palhoça"))
        myClients.add(Client("Amanda", "Palhoça"))
        myClients.add(Client("Amanda", "Palhoça"))
        myClients.add(Client("Pedro", "Palhoça"))
        myClients.add(Client("Rosa", "Palhoça"))
        myClients.add(Client("Joaquim", "Palhoça"))
        myClients.add(Client("Vinicius", "Palhoça"))
        myClients.add(Client("Alfredo", "Palhoça"))
        myClients.add(Client("Marcos", "Palhoça"))
        myClients.add(Client("Kauã", "Palhoça"))
        myClients.add(Client("Amanda", "Palhoça"))

        clients.postValue(myClients)
    }

    fun onClientClicked(client: Client){
        _navigateToAdd.value = true
    }

}

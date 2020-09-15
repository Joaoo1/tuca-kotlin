package com.joaovitor.tucaprodutosdelimpeza.ui.client.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import java.util.*

class ClientListViewModel : ViewModel() {

    var clients: MutableLiveData<List<Client>>
    var clientSales: MutableLiveData<List<Sale>>

    init {
        val emptyList: List<Client> = emptyList()
        val emptyList2: List<Sale> = emptyList()
        clients = MutableLiveData(emptyList)
        clientSales = MutableLiveData(emptyList2)

        setClients()
        setClienSales()
    }

    private fun setClients() {
        val myProducts = mutableListOf<Client>()
        myProducts.add(Client("asd", "Amanda", "Palhoça"))
        myProducts.add(Client("asd2", "Alfredo", "Palhoça"))
        myProducts.add(Client("asd3", "Joaquim", "Palhoça"))
        myProducts.add(Client("as4d", "Pedro", "Palhoça"))
        myProducts.add(Client("asd5", "Rosa", "Palhoça"))
        myProducts.add(Client("asd7", "Vinicius", "Palhoça"))
        myProducts.add(Client("asd8", "Marcos", "Palhoça"))
        myProducts.add(Client("asd0", "Kauã", "Palhoça"))

        clients.postValue(myProducts)
    }

    private fun setClienSales() {
        val products = ArrayList<Product>()
        products.add(Product("Detergente automotivo com Cera 5L", "20.00",100, 2))
        products.add(Product("Detergente neutro", "40.00",200, 1))
        products.add(Product("Amaciante", "10.00",100, 1))
        val sales = mutableListOf<Sale>()
/*        sales.add(Sale("asd", 4500,Client("1","Amanda","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 4500,Client("1","Bernardo","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 4520,Client("1","Bruno","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 1200,Client("1","Carlos","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 1240,Client("1","Caroline","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 2130,Client("1","João","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 1500,Client("1","Pablo","Palhoça"), Date(),
            "300.00", false, products))
        sales.add(Sale("asd", 5500,Client("1","Valter","Palhoça"), Date(),
            "300.00", false, products))*/

        clientSales.postValue(sales)
    }

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

    fun doneNavigation(){
        _navigateToAdd.value = false
        _navigateToEdit.value = null
        _openInfoDialog.value = null
    }

    fun onClientClicked(client: Client) {
        _openInfoDialog.value = client
    }

    fun openEditClient() {
        _navigateToEdit.value = Client("Rrr","asd")
    }

}

package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientAddViewModel : ViewModel() {

    var client = MutableLiveData(Client())

    private var _streets = MutableLiveData<List<String>>()
    val streets: LiveData<List<String>>
        get() = _streets

    private var _neighborhoods: MutableList<String> = mutableListOf()
    val neighborhoods: Array<String>
        get() = _neighborhoods.toTypedArray()

    private var _cities: MutableList<String> = mutableListOf()
    val cities: Array<String>
        get() = _cities.toTypedArray()

    init {
        GlobalScope.launch {
            _streets.postValue(StreetRepository().getStreets())
            _neighborhoods.addAll(NeighborhoodRepository().getNeighborhoods())
            _cities.addAll(CityRepository().getCities())
        }
    }

    //navigation
    private var _navigateToManageAddress = MutableLiveData<Boolean>()
    val navigateToManageAddress: LiveData<Boolean>
        get() = _navigateToManageAddress

    fun doneNavigation(){
        _navigateToManageAddress.value = false
    }

    fun onClickMenuItemAddAddress() {
        _navigateToManageAddress.value = true
    }

    fun onNeighborhoodSelected(neighborhood: String) {
        client.value?.neighborhood = neighborhood
        client.value = client.value
    }

    fun onCitySelected(city: String) {
        client.value?.city = city
        client.value = client.value
    }
}

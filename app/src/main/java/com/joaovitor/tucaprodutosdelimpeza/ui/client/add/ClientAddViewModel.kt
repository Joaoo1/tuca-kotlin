package com.joaovitor.tucaprodutosdelimpeza.ui.client.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientAddViewModel : ViewModel() {

    private val clientRepository = ClientRepository()

    var client = MutableLiveData(Client())

    private var _streets = MutableLiveData<List<Street>>()
    val streets: LiveData<List<Street>>
        get() = _streets

    private var _neighborhoods: MutableList<Neighborhood> = mutableListOf()
    val neighborhoods: Array<Neighborhood>
        get() = _neighborhoods.toTypedArray()

    private var _cities: MutableList<City> = mutableListOf()
    val cities: Array<City>
        get() = _cities.toTypedArray()

    private var _navigateToManageAddress = MutableLiveData<Boolean>()
    val navigateToManageAddress: LiveData<Boolean>
        get() = _navigateToManageAddress

    private var _openSelectNeighborhood = MutableLiveData<Boolean>()
    val openSelectNeighborhood: LiveData<Boolean>
        get() = _openSelectNeighborhood

    private var _openSelectCity = MutableLiveData<Boolean>()
    val openSelectCity: LiveData<Boolean>
        get() = _openSelectCity

    fun fetchAddress() {
        GlobalScope.launch {
            _streets.postValue(StreetRepository().getStreets())
            _neighborhoods.addAll(NeighborhoodRepository().getNeighborhoods())
            _cities.addAll(CityRepository().getCities())
        }
    }

    /** Save new client on database */
    private fun addClient(){
        /**
         * Client must at least have a name
         */
        if(client.value!!.name.isEmpty()){
            //TODO: Show a error: Need to inform a name
            return
        }

        /**
         * Check if a street has been informed,
         * If so verify if the informed street is already registered
         */
        if(client.value!!.street.isNotEmpty() &&
            streets.value!!.toTypedArray().indexOf(client.value!!.street) == -1){
            //TODO: Show a error: street not found
            return
        }

        GlobalScope.launch {
            val result = clientRepository.addClient(client.value!!)

            if(result is Result.Success) {
                //TODO: Show success message
                client.postValue(Client())
            } else {
                //TODO: Show error message
                return@launch
            }
        }
    }

    fun onClickAddAddress() {
        _navigateToManageAddress.value = true
    }

    fun onClickSelectNeighborhood(){
        _openSelectNeighborhood.value = true
    }

    fun onClickSelectCity(){
        _openSelectCity.value = true
    }

    fun onNeighborhoodSelected(neighborhood: String) {
        client.value?.neighborhood = neighborhood
        client.value = client.value
    }

    fun onCitySelected(city: String) {
        client.value?.city = city
        client.value = client.value
    }

    fun onClickSave(){
        //TODO: Show a progress bar
        addClient()
    }

    fun doneNavigation(){
        _navigateToManageAddress.value = false
        _openSelectNeighborhood.value = false
        _openSelectCity.value = false
    }
}

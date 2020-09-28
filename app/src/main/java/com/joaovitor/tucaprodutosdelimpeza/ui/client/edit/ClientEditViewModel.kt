package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.data.model.Product
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ClientEditViewModel(var mClient: Client) : ViewModel() {

    private val clientRepository = ClientRepository()

    var client = MutableLiveData<Client>(mClient.copy())

    private var _streets = MutableLiveData<List<String>>()
    val streets: LiveData<List<String>>
        get() = _streets

    private var _neighborhoods: MutableList<String> = mutableListOf()
    val neighborhoods: Array<String>
        get() = _neighborhoods.toTypedArray()

    private var _cities: MutableList<String> = mutableListOf()
    val cities: Array<String>
        get() = _cities.toTypedArray()

    private var _openSelectNeighborhood = MutableLiveData<Boolean>()
    val openSelectNeighborhood: LiveData<Boolean>
        get() = _openSelectNeighborhood

    private var _openSelectCity = MutableLiveData<Boolean>()
    val openSelectCity: LiveData<Boolean>
        get() = _openSelectCity

    private var _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    private var _navigateToManageAddress = MutableLiveData<Boolean>()
    val navigateToManageAddress: LiveData<Boolean>
        get() = _navigateToManageAddress

    init {
        GlobalScope.launch {
            _streets.postValue(StreetRepository().getStreets())
            _neighborhoods.addAll(NeighborhoodRepository().getNeighborhoods())
            _cities.addAll(CityRepository().getCities())
        }
    }

    private fun deleteClient(){
        GlobalScope.launch {
            val result = clientRepository.deleteClient(client.value!!.id)
            if (result is Result.Success) {
                //TODO: Show a success message
                _navigateBack.postValue(true)
            }else {
                //TODO: Show a error message
                return@launch
            }
        }
    }

    private fun editClient(){
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
            val result = clientRepository.editClient(client.value!!)
            if (result is Result.Success) {
                mClient.bind(client.value!!)
                _navigateBack.postValue(true)
                //TODO: Show a success message
            }else {
                //TODO: Show a error message
                return@launch
            }
        }
    }

    fun onNeighborhoodSelected(neighborhood: String) {
        client.value?.neighborhood = neighborhood
        client.value = client.value
    }

    fun onCitySelected(city: String) {
        client.value?.city = city
        client.value = client.value
    }

    fun doneNavigation() {
        _navigateToManageAddress.value = false
        _navigateBack.value = false
    }

    fun onClickMenuItemAddAddress() {
        _navigateToManageAddress.value = true
    }

    fun onClickSelectNeighborhood(){
        _openSelectNeighborhood.value = true
    }

    fun onClickSelectCity(){
        _openSelectCity.value = true
    }

    fun onClickDeleteClient() {
        deleteClient()
    }

    fun onClickSave() {
        editClient()
    }
}

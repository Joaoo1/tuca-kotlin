package com.joaovitor.tucaprodutosdelimpeza.ui.client.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.ClientRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientEditViewModel(private var mClient: Client) : BaseViewModel() {

    private val clientRepository = ClientRepository()

    var client = MutableLiveData(mClient.copy())

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
            val resultStreets = StreetRepository().getStreets()
            if(resultStreets is Result.Success) {
                _streets.postValue(resultStreets.data?.map { it.name })
            } else {
                _error.postValue("Erro ao buscar ruas")
            }

            val resultNeighborhood = NeighborhoodRepository().getNeighborhoods()
            if(resultNeighborhood is Result.Success) {
                _neighborhoods.addAll(resultNeighborhood.data!!.map { it.name })
            } else {
                _error.postValue("Erro ao buscar bairros")
            }

            val resultCities = CityRepository().getCities()
            if(resultCities is Result.Success) {
                _cities.addAll(resultCities.data!!.map { it.name })
            } else {
                _error.postValue("Erro ao buscar cidades")
            }
        }
    }

    private fun deleteClient(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = clientRepository.deleteClient(client.value!!.id)
            if (result is Result.Success) {
                _info.postValue("Cliente excluído com sucesso")
                _navigateBack.postValue(true)
            }else {
                _error.postValue("Erro ao excluir cliente")
            }

            _showProgressBar.postValue(false)
        }
    }

    private fun editClient(){
        /**
         * Client must at least have a name
         */
        if(client.value!!.name.isEmpty()){
            _error.postValue("Informe um nome para o cliente")
            return
        }

        /**
         * Check if a street has been informed,
         * If so verify if the informed street is already registered
         */
        if(client.value!!.street.isNotEmpty() &&
            streets.value!!.toTypedArray().indexOf(client.value!!.street) == -1){
            _error.postValue("Rua não encontrada")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = clientRepository.editClient(client.value!!)
            if (result is Result.Success) {
                _info.postValue("Cliente editado com sucesso")
                mClient.bind(client.value!!)
                _navigateBack.postValue(true)
            }else {
                _error.postValue("Erro ao editar cliente! Contate o desenvolvedor!")
            }

            _showProgressBar.postValue(false)
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

    fun doneNavigating() {
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

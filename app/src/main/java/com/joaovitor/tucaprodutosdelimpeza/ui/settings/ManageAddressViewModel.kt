package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import com.joaovitor.tucaprodutosdelimpeza.ui.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ManageAddressViewModel : BaseViewModel() {

    data class AddressObject(var addressType: AddressType, var addressList: List<Address>)

    private var streets: MutableList<Street> = mutableListOf()

    private var neighborhoods: MutableList<Neighborhood> = mutableListOf()

    private var cities: MutableList<City> = mutableListOf()

    private var _openDialogAddAddress = MutableLiveData<AddressType>()
    val openDialogAddAddress: LiveData<AddressType>
        get() = _openDialogAddAddress

    private var _openDialogEditAddress = MutableLiveData<AddressObject>()
    val openDialogEditAddress: LiveData<AddressObject>
        get() = _openDialogEditAddress

    private var _openDialogDeleteAddress = MutableLiveData<AddressObject>()
    val openDialogDeleteAddress: LiveData<AddressObject>
        get() = _openDialogDeleteAddress

    fun onClickAddStreet(){
        _openDialogAddAddress.value = AddressType.STREET
    }

    fun onClickAddNeighborhood(){
        _openDialogAddAddress.value = AddressType.NEIGHBORHOOD
    }

    fun onClickAddCity(){
        _openDialogAddAddress.value = AddressType.CITY
    }

    fun onClickEditStreet(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultStreet = StreetRepository().getStreets()
            if(resultStreet is Result.Success) {
                streets = resultStreet.data?.toMutableList()!!
                _openDialogEditAddress.postValue(AddressObject(AddressType.STREET, streets.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar ruas!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickEditNeighborhood() {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultNeighborhood = NeighborhoodRepository().getNeighborhoods()
            if(resultNeighborhood is Result.Success) {
                neighborhoods = resultNeighborhood.data?.toMutableList()!!
                _openDialogEditAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar bairrps!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickEditCity(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultCities = CityRepository().getCities()
            if(resultCities is Result.Success) {
                cities = resultCities.data?.toMutableList()!!
                _openDialogEditAddress.postValue(AddressObject(AddressType.CITY, cities.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar cidades!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickDeleteStreet(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultStreet = StreetRepository().getStreets()
            if(resultStreet is Result.Success) {
                streets = resultStreet.data?.toMutableList()!!
                _openDialogDeleteAddress.postValue(AddressObject(AddressType.STREET, streets.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar ruas!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickDeleteNeighborhood(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultNeighborhood = NeighborhoodRepository().getNeighborhoods()
            if(resultNeighborhood is Result.Success) {
                neighborhoods = resultNeighborhood.data?.toMutableList()!!
                _openDialogDeleteAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar bairros!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickDeleteCity(){
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val resultCities = CityRepository().getCities()
            if(resultCities is Result.Success) {
                cities = resultCities.data?.toMutableList()!!
                _openDialogDeleteAddress.postValue(AddressObject(AddressType.CITY, cities.map{it.copy()}))
            } else {
                _error.postValue("Erro ao carregar cidades!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun dialogDoneOpening() {
        _openDialogAddAddress.value = null
        _openDialogEditAddress.value = null
        _openDialogDeleteAddress.value = null
    }

    fun onClickAddAddressPositiveButton(addressName: String, type: AddressType) {
        if(addressName.isEmpty()) {
            _error.postValue("Informe o nome do endereço!")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = when(type){
                AddressType.STREET -> StreetRepository().addStreet(Street("", addressName))
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().addNeighborhood(Neighborhood("", addressName))
                AddressType.CITY -> CityRepository().addCity(City("", addressName))
            }

            if(result is Result.Success) {
                _info.postValue("Endereço adicionado com sucesso")
            } else {
                _error.postValue("Erro ao adicionar endereço!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickEditAddressPositiveButton(address: Address, newName:String) {
        if(newName.isEmpty()) {
            _error.postValue("Informe o nome do endereço!")
            return
        }

        val addressAlreadyExists: Int = when(address.type!!) {
            AddressType.STREET -> streets.map { it.name }.indexOf(newName)
            AddressType.NEIGHBORHOOD -> neighborhoods.map { it.name }.indexOf(newName)
            AddressType.CITY -> cities.map { it.name }.indexOf(newName)
        }

        if(addressAlreadyExists != -1) {
            _error.postValue("Este endereço já está cadastrado")
            return
        }

        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = when(address.type!!){
                AddressType.STREET -> StreetRepository().editStreet(address as Street, newName)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().editNeighborhood(address as Neighborhood, newName)
                AddressType.CITY -> CityRepository().editCity(address as City, newName)
            }

            if(result is Result.Success) {
                _info.postValue("Endereço editado com sucesso")
            } else {
                _error.postValue("Erro ao editar endereço!")
            }

            _showProgressBar.postValue(false)
        }
    }

    fun onClickDeleteAddressPositiveButton(address: Address) {
        GlobalScope.launch {
            _showProgressBar.postValue(true)

            val result = when(address.type!!){
                AddressType.STREET -> StreetRepository().deleteStreet(address.id)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().deleteNeighborhood(address.id)
                AddressType.CITY -> CityRepository().deleteCity(address.id)
            }

            if(result is Result.Success) {
                _info.postValue("Endereço excluído com sucesso!")
            } else {
                _error.postValue("Erro ao excluir endereço!")
            }

            _showProgressBar.postValue(false)
        }
    }
}

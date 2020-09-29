package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ManageAddressViewModel : ViewModel() {

    data class AddressObject(var addressType: AddressType, var addressList: List<Address>)

    enum class AddressType(val value: String) {
        STREET("rua"),
        NEIGHBORHOOD("bairro"),
        CITY("cidade"),
    }

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
            streets = StreetRepository().getStreets().toMutableList()
            _openDialogEditAddress.postValue(AddressObject(AddressType.STREET, streets.map{it.copy()}))
        }
    }

    fun onClickEditNeighborhood(){
        GlobalScope.launch {
            neighborhoods = NeighborhoodRepository().getNeighborhoods().toMutableList()
            _openDialogEditAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map{it.copy()}))
        }
    }

    fun onClickEditCity(){
        GlobalScope.launch {
            cities = CityRepository().getCities().toMutableList()
            _openDialogEditAddress.postValue(AddressObject(AddressType.CITY, cities.map{it.copy()}))
        }
    }

    fun onClickDeleteStreet(){
        GlobalScope.launch {
            streets = StreetRepository().getStreets().toMutableList()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.STREET, streets.map{it.copy()}))
        }
    }

    fun onClickDeleteNeighborhood(){
        GlobalScope.launch {
            neighborhoods = NeighborhoodRepository().getNeighborhoods().toMutableList()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map{it.copy()}))
        }
    }

    fun onClickDeleteCity(){
        GlobalScope.launch {
            cities = CityRepository().getCities().toMutableList()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.CITY, cities.map{it.copy()}))
        }
    }

    fun dialogDoneOpening() {
        _openDialogAddAddress.value = null
        _openDialogEditAddress.value = null
        _openDialogDeleteAddress.value = null
    }

    fun onClickAddAddressPositiveButton(addressName: String, type: AddressType) {
        if(addressName.isEmpty()) {
            //TODO: Show error message: field name can't be empty
            return
        }

        GlobalScope.launch {
            val result = when(type){
                AddressType.STREET -> StreetRepository().addStreet(Street("", addressName))
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().addNeighborhood(Neighborhood("", addressName))
                AddressType.CITY -> CityRepository().addCity(City("", addressName))
            }

            if(result is Result.Success) {
                //TODO: Show success message
            } else {
                //TODO: Show error message
                return@launch
            }
        }
    }

    fun onClickEditAddressPositiveButton(address: Address, type: AddressType) {
        if(address.name.isEmpty()) {
            //TODO: Show error message: field name can't be empty
            return
        }

        val addressAlreadyExists: Int = when(type) {
            AddressType.STREET -> streets.map { it.name }.indexOf(address.name)
            AddressType.NEIGHBORHOOD -> neighborhoods.map { it.name }.indexOf(address.name)
            AddressType.CITY -> cities.map { it.name }.indexOf(address.name)
        }

        if(addressAlreadyExists != -1) {
            //TODO: Show error message: address already exists
            return
        }

        GlobalScope.launch {
            val result = when(type){
                AddressType.STREET -> StreetRepository().editStreet(address as Street)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().editNeighborhood(address as Neighborhood)
                AddressType.CITY -> CityRepository().editCity(address as City)
            }

            if(result is Result.Success) {
                //TODO: Show success message
            } else {
                //TODO: Show error message
                return@launch
            }
        }
    }

    fun onClickDeleteAddressPositiveButton(addressId: String, type: AddressType) {
        GlobalScope.launch {
            val result = when(type){
                AddressType.STREET -> StreetRepository().deleteStreet(addressId)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().deleteNeighborhood(addressId)
                AddressType.CITY -> CityRepository().deleteCity(addressId)
            }

            if(result is Result.Success) {
                //TODO: Show success message
            } else {
                //TODO: Show error message
                return@launch
            }
        }
    }
}

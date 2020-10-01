package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.Result
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import com.joaovitor.tucaprodutosdelimpeza.data.model.Address
import com.joaovitor.tucaprodutosdelimpeza.data.model.AddressType
import com.joaovitor.tucaprodutosdelimpeza.data.model.Street
import com.joaovitor.tucaprodutosdelimpeza.data.model.Neighborhood
import com.joaovitor.tucaprodutosdelimpeza.data.model.City
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ManageAddressViewModel : ViewModel() {

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

    fun onClickEditAddressPositiveButton(address: Address, newName:String) {
        if(newName.isEmpty()) {
            //TODO: Show error message: field name can't be empty
            return
        }

        val addressAlreadyExists: Int = when(address.type!!) {
            AddressType.STREET -> streets.map { it.name }.indexOf(newName)
            AddressType.NEIGHBORHOOD -> neighborhoods.map { it.name }.indexOf(newName)
            AddressType.CITY -> cities.map { it.name }.indexOf(newName)
        }

        if(addressAlreadyExists != -1) {
            //TODO: Show error message: address already exists
            return
        }

        GlobalScope.launch {
            val result = when(address.type!!){
                AddressType.STREET -> StreetRepository().editStreet(address as Street, newName)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().editNeighborhood(address as Neighborhood, newName)
                AddressType.CITY -> CityRepository().editCity(address as City, newName)
            }

            if(result is Result.Success) {
                //TODO: Show success message
            } else {
                //TODO: Show error message
                return@launch
            }
        }
    }

    fun onClickDeleteAddressPositiveButton(address: Address) {
        GlobalScope.launch {
            val result = when(address.type!!){
                AddressType.STREET -> StreetRepository().deleteStreet(address.id)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().deleteNeighborhood(address.id)
                AddressType.CITY -> CityRepository().deleteCity(address.id)
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

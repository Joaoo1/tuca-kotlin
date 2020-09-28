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

    private var streets: List<Street> = emptyList()

    private var neighborhoods: List<Neighborhood> = emptyList()

    private var cities: List<City> = emptyList()

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
            streets = StreetRepository().getStreets()
            _openDialogEditAddress.postValue(AddressObject(AddressType.STREET, streets.map { it.name }))
        }
    }

    fun onClickEditNeighborhood(){
        GlobalScope.launch {
            neighborhoods = NeighborhoodRepository().getNeighborhoods()
            _openDialogEditAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map { it.name }))
        }
    }

    fun onClickEditCity(){
        GlobalScope.launch {
            cities = CityRepository().getCities()
            _openDialogEditAddress.postValue(AddressObject(AddressType.CITY, cities.map { it.name }))
        }
    }

    fun onClickDeleteStreet(){
        GlobalScope.launch {
            streets = StreetRepository().getStreets()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.STREET, streets.map { it.name }))
        }
    }

    fun onClickDeleteNeighborhood(){
        GlobalScope.launch {
            neighborhoods = NeighborhoodRepository().getNeighborhoods()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.NEIGHBORHOOD, neighborhoods.map { it.name }))
        }
    }

    fun onClickDeleteCity(){
        GlobalScope.launch {
            cities = CityRepository().getCities()
            _openDialogDeleteAddress.postValue(AddressObject(AddressType.CITY, cities.map { it.name }))
        }
    }

    fun dialogDoneOpening() {
        _openDialogAddAddress.value = null
        _openDialogEditAddress.value = null
        _openDialogDeleteAddress.value = null
    }

    fun onClickAddAddresstPositiveButton(addressName: String, type: AddressType) {
        if(addressName.isEmpty()) {
            //TODO: Show error message: field name can't be empty
            return
        }

        GlobalScope.launch {
            val result = when(type){
                AddressType.STREET -> StreetRepository().addStreet(addressName)
                AddressType.NEIGHBORHOOD -> NeighborhoodRepository().addNeighborhood(addressName)
                AddressType.CITY -> CityRepository().addCity(addressName)
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

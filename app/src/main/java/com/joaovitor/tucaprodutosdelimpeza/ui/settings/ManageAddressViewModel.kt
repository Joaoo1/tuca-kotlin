package com.joaovitor.tucaprodutosdelimpeza.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joaovitor.tucaprodutosdelimpeza.data.CityRepository
import com.joaovitor.tucaprodutosdelimpeza.data.NeighborhoodRepository
import com.joaovitor.tucaprodutosdelimpeza.data.StreetRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ManageAddressViewModel : ViewModel() {

    data class AddressObject(var addressType: AddressType, var addressList: List<String>)

    enum class AddressType(val value: String) {
        STREET("rua"),
        NEIGHBORHOOD("bairro"),
        CITY("cidade"),
    }


    private var _openDialogAddAddress = MutableLiveData<AddressType>()
    val openDialogAddAddress: LiveData<AddressType>
        get() = _openDialogAddAddress

    private var _openDialogEditAddress = MutableLiveData<AddressObject>()
    val openDialogEditAddress: LiveData<AddressObject>
        get() = _openDialogEditAddress

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
            val streets = StreetRepository().getStreets()
            _openDialogEditAddress.value = AddressObject(AddressType.STREET, streets)
        }
    }

    fun onClickEditNeighborhood(){
        GlobalScope.launch {
            val neighborhoods = NeighborhoodRepository().getNeighborhoods()
            _openDialogEditAddress.value = AddressObject(AddressType.NEIGHBORHOOD, neighborhoods)
        }
    }

    fun onClickEditCity(){
        GlobalScope.launch {
            val cities = CityRepository().getCities()
            _openDialogEditAddress.value = AddressObject(AddressType.CITY, cities)
        }
    }

    fun dialogDoneOpening() {
        _openDialogAddAddress.value = null
        _openDialogEditAddress.value = null
    }
}

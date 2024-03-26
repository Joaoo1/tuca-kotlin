package com.joaovitor.tucaprodutosdelimpeza.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected var _showProgressBar = MutableLiveData(false)
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    protected var _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    protected var _info = MutableLiveData<String?>()
    val info: LiveData<String?>
        get() = _info

    fun doneShowError() {
        _error.value = null
    }

    fun doneShowInfo() {
        _info.value = null
    }
}
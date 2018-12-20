package com.amperas17.mangaedenapp.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.amperas17.mangaedenapp.model.Resource
import com.amperas17.mangaedenapp.utils.Caller

open class BaseViewModel<T> : ViewModel(), Caller<T> {

    val resource: MutableLiveData<Resource<T>> = MutableLiveData()

    protected var isLoading: Boolean = false

    open fun getResource(): LiveData<Resource<T>> {
        return resource
    }

    //override in inherit
    open fun startLoading() {
        if (isLoading) return
        isLoading = true
    }

    //override in inherit
    open fun stopLoading() {
        if (!isLoading) return
        isLoading = false
    }

    override fun onGetData(data: T) {
        resource.postValue(Resource(data))
        isLoading = false
    }

    override fun onError(t: Throwable) {
        resource.postValue(Resource(t))
        isLoading = false
    }

    override fun onCleared() {
        super.onCleared()
        stopLoading()
    }
}

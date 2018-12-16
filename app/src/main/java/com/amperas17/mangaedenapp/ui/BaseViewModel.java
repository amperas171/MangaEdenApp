package com.amperas17.mangaedenapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.utils.Caller;

public class BaseViewModel<T> extends ViewModel implements Caller<T> {

    private MutableLiveData<Resource<T>> resource;

    protected boolean isLoading;

    public LiveData<Resource<T>> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        return resource;
    }

    //override in inherit
    public void startLoading() {
        if (isLoading) return;
        isLoading = true;
    }

    //override in inherit
    public void stopLoading() {
        if (!isLoading) return;
        isLoading = false;
    }

    @Override
    public void onGetData(T mangaFullInfo) {
        resource.postValue(new Resource<>(mangaFullInfo));
        isLoading = false;
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new Resource<T>(t));
        isLoading = false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLoading();
    }
}

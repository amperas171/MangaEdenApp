package com.amperas17.mangaedenapp.ui.mangafull.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.data.MangaFullRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;

public class MangaFullViewModel extends ViewModel implements MangaFullRepository.IGetMangaFull {

    private MutableLiveData<Resource<MangaFullInfo>> resource;
    private MangaFullRepository repository;

    private boolean isLoading;

    public LiveData<Resource<MangaFullInfo>> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        if (repository == null) {
            repository = new MangaFullRepository(this);
        }
        return resource;
    }

    private void loadData(String mangaID) {
        isLoading = true;
        repository.callData(mangaID);
    }

    public void startLoading(String mangaID) {
        if (!isLoading)
            loadData(mangaID);
    }

    public void stopLoading() {
        if (isLoading)
            repository.cancelDataRequest();
    }

    @Override
    public void onGetData(MangaFullInfo mangaFullInfo) {
        resource.postValue(new Resource<>(mangaFullInfo));
        isLoading = false;
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new Resource<MangaFullInfo>(t));
        isLoading = false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLoading();
    }
}

package com.amperas17.mangaedenapp.ui.mangalist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.data.MangaListProvider;
import com.amperas17.mangaedenapp.model.manga.Manga;

import java.util.ArrayList;

public class MangaListViewModel extends ViewModel implements MangaListProvider.IGetMangaList {

    private MutableLiveData<MangaListResource> resource;
    private MangaListProvider mangaListProvider;

    private boolean isLoading;

    public LiveData<MangaListResource> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        if (mangaListProvider == null) {
            mangaListProvider = new MangaListProvider(this);
        }
        return resource;
    }

    private void loadData() {
        isLoading = true;
        mangaListProvider.callData();
    }

    public void startLoading() {
        if (!isLoading)
            loadData();
    }

    public void stopLoading() {
        if (isLoading)
            mangaListProvider.cancelDataRequest();
    }

    @Override
    public void onGetData(ArrayList<Manga> mangaList) {
        resource.postValue(new MangaListResource(mangaList));
        isLoading = false;
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new MangaListResource(t));
        isLoading = false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLoading();
    }
}

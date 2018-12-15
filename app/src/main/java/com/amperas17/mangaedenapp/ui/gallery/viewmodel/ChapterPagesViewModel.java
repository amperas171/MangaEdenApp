package com.amperas17.mangaedenapp.ui.gallery.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.data.ChapterRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.utils.Caller;

import java.util.ArrayList;


public class ChapterPagesViewModel extends ViewModel implements Caller<ArrayList<Page>> {

    private MutableLiveData<Resource<ArrayList<Page>>> resource;
    private ChapterRepository repository;

    private boolean isLoading;

    public LiveData<Resource<ArrayList<Page>>> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        if (repository == null) {
            repository = new ChapterRepository(this);
        }
        return resource;
    }

    private void loadData(String chapterID) {
        isLoading = true;
        repository.callData(chapterID);
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
    public void onGetData(ArrayList<Page> mangaFullInfo) {
        resource.postValue(new Resource<>(mangaFullInfo));
        isLoading = false;
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new Resource<ArrayList<Page>>(t));
        isLoading = false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLoading();
    }
}

package com.amperas17.mangaedenapp.ui.mangalist.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.data.MangaListRepository;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.utils.Caller;


import java.util.ArrayList;

public class MangaListViewModel extends ViewModel implements Caller<ArrayList<Manga>>, FindHandler.Caller {

    private MutableLiveData<Resource<ArrayList<Manga>>> resource;
    private MangaListRepository repository;
    private FindHandler handler;

    private ArrayList<Manga> mangaListAll = new ArrayList<>();
    private boolean isLoading;

    public LiveData<Resource<ArrayList<Manga>>> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        if (repository == null) {
            repository = new MangaListRepository(this);
        }
        if (handler == null) {
            handler = new FindHandler(this);
        }
        return resource;
    }

    private void loadData() {
        isLoading = true;
        repository.callData();
    }

    public void startLoading() {
        if (!isLoading)
            loadData();
    }

    public void stopLoading() {
        if (isLoading)
            repository.cancelDataRequest();
    }

    @Override
    public void onGetData(ArrayList<Manga> mangaList) {
        resource.postValue(new Resource<>(mangaList));
        updateMangaListAll(mangaList);
        isLoading = false;
    }

    private void updateMangaListAll(ArrayList<Manga> mangaList) {
        mangaListAll.clear();
        mangaListAll.addAll(mangaList);
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new Resource<ArrayList<Manga>>(t));
        isLoading = false;
    }


    public void findData(final String pattern) {
        if (!mangaListAll.isEmpty()) {
            if (pattern.isEmpty()) {
                postSearchResult(mangaListAll);
            } else {
                runSearchInNewThread(pattern);
                isLoading = true;
            }
        }
    }

    @Override
    public void postSearchResult(ArrayList<Manga> mangaList) {
        resource.postValue(new Resource<>(mangaList));
        isLoading = false;
    }

    private void runSearchInNewThread(final String pattern) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.setResultList(MangaListUtils.findForMatchesInTitle(pattern, mangaListAll));
                handler.sendEmptyMessage(0);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLoading();
        handler.removeMessages(0);
    }
}

package com.amperas17.mangaedenapp.ui.mangalist.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.amperas17.mangaedenapp.data.MangaListRepository;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.ui.mangalist.model.MangaListResource;

import java.util.ArrayList;

public class MangaListViewModel extends ViewModel implements MangaListRepository.IGetMangaList, FindHandler.Caller {

    private MutableLiveData<MangaListResource> resource;
    private MangaListRepository mangaListRepository;
    private FindHandler handler;

    private ArrayList<Manga> mangaListAll = new ArrayList<>();
    private boolean isLoading;

    public LiveData<MangaListResource> getResource() {
        if (resource == null) {
            resource = new MutableLiveData<>();
        }
        if (mangaListRepository == null) {
            mangaListRepository = new MangaListRepository(this);
        }
        if (handler == null) {
            handler = new FindHandler(this);
        }
        return resource;
    }

    private void loadData() {
        isLoading = true;
        mangaListRepository.callData();
    }

    public void startLoading() {
        if (!isLoading)
            loadData();
    }

    public void stopLoading() {
        if (isLoading)
            mangaListRepository.cancelDataRequest();
    }

    @Override
    public void onGetData(ArrayList<Manga> mangaList) {
        resource.postValue(new MangaListResource(mangaList));
        updateMangaListAll(mangaList);
        isLoading = false;
    }

    private void updateMangaListAll(ArrayList<Manga> mangaList){
        mangaListAll.clear();
        mangaListAll.addAll(mangaList);
    }

    @Override
    public void onError(Throwable t) {
        resource.postValue(new MangaListResource(t));
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
        resource.postValue(new MangaListResource(mangaList));
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

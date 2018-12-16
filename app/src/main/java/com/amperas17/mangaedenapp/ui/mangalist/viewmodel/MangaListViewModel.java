package com.amperas17.mangaedenapp.ui.mangalist.viewmodel;

import android.arch.lifecycle.LiveData;

import com.amperas17.mangaedenapp.data.MangaListRepository;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.ui.BaseViewModel;


import java.util.ArrayList;

public class MangaListViewModel extends BaseViewModel<ArrayList<Manga>> implements FindHandler.Caller {

    private MangaListRepository repository;
    private FindHandler handler;

    private ArrayList<Manga> mangaListAll = new ArrayList<>();

    public LiveData<Resource<ArrayList<Manga>>> getResource() {
        if (repository == null) {
            repository = new MangaListRepository(this);
        }
        if (handler == null) {
            handler = new FindHandler(this);
        }
        return super.getResource();
    }

    private void loadData() {
        repository.callData();
    }

    public void startLoading() {
        super.startLoading();
        loadData();
    }

    public void stopLoading() {
        super.stopLoading();
        repository.cancelDataRequest();
    }

    @Override
    public void onGetData(ArrayList<Manga> mangaList) {
        updateMangaListAll(mangaList);
        super.onGetData(mangaList);
    }

    private void updateMangaListAll(ArrayList<Manga> mangaList) {
        mangaListAll.clear();
        mangaListAll.addAll(mangaList);
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
        super.onGetData(mangaList);
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
        handler.removeMessages(0);
    }
}

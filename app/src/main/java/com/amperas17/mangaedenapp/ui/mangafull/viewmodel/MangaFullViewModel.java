package com.amperas17.mangaedenapp.ui.mangafull.viewmodel;

import android.arch.lifecycle.LiveData;

import com.amperas17.mangaedenapp.data.MangaFullRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.ui.BaseViewModel;


public class MangaFullViewModel extends BaseViewModel<MangaFullInfo> {

    private MangaFullRepository repository;

    public LiveData<Resource<MangaFullInfo>> getResource() {
        if (repository == null) {
            repository = new MangaFullRepository(this);
        }
        return super.getResource();
    }

    private void loadData(String mangaID) {
        repository.callData(mangaID);
    }

    public void startLoading(String mangaID) {
        super.startLoading();
        loadData(mangaID);
    }

    public void stopLoading() {
        super.stopLoading();
        repository.cancelDataRequest();
    }
}

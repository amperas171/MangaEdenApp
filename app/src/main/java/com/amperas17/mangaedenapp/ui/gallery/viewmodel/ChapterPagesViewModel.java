package com.amperas17.mangaedenapp.ui.gallery.viewmodel;

import android.arch.lifecycle.LiveData;

import com.amperas17.mangaedenapp.data.ChapterRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.ui.BaseViewModel;

import java.util.ArrayList;


public class ChapterPagesViewModel extends BaseViewModel<ArrayList<Page>> {

    private ChapterRepository repository;

    public LiveData<Resource<ArrayList<Page>>> getResource() {
        if (repository == null) {
            repository = new ChapterRepository(this);
        }
        return super.getResource();
    }

    private void loadData(String chapterID) {
        repository.callData(chapterID);
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

package com.amperas17.mangaedenapp.ui.mangalist.viewmodel;

import android.os.Handler;
import android.os.Message;

import com.amperas17.mangaedenapp.model.manga.Manga;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FindHandler extends Handler {
    private WeakReference<Caller> callerRef;
    private final ArrayList<Manga> resultList = new ArrayList<>();

    FindHandler(Caller caller) {
        this.callerRef = new WeakReference<>(caller);
    }

    void setResultList(ArrayList<Manga> list) {
        resultList.clear();
        resultList.addAll(list);
    }

    @Override
    public void handleMessage(Message msg) {
        if (callerRef != null)
            callerRef.get().postSearchResult(resultList);
    }

    interface Caller {
        void postSearchResult(ArrayList<Manga> mangaList);
    }
}
package com.amperas17.mangaedenapp.ui.mangalist;


import android.content.Context;

import com.amperas17.mangaedenapp.data.BaseResponseProvider;
import com.amperas17.mangaedenapp.data.MangaListResponseProvider;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MangaListProvider implements BaseResponseProvider.IGetData<MangaListResponse> {

    Context context;
    MangaListResponseProvider responseProvider;

    public MangaListProvider(Context context){
        this.context = context;
        responseProvider = new MangaListResponseProvider(this);
    }

    @Override
    public void onGetDate(MangaListResponse response) {
        ArrayList<Manga> list = response.getMangas();
        Collections.sort(list);
        ((IGetMangaList)context).onGetDate(list);
    }

    @Override
    public void onError(Throwable t) {
        ((IGetMangaList)context).onError(t);
    }

    public void callData(){
        responseProvider.makeCall();
    }

    public void cancelDataRequest(){
        responseProvider.stopRequest();
    }

    public interface IGetMangaList{
        void onGetDate(ArrayList<Manga> mangaList);
        void onError(Throwable t);
    }
}

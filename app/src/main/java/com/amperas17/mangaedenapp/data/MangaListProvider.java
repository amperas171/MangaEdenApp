package com.amperas17.mangaedenapp.data;


import com.amperas17.mangaedenapp.api.responseprovider.BaseResponseProvider;
import com.amperas17.mangaedenapp.api.responseprovider.MangaListResponseProvider;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;

import java.util.ArrayList;
import java.util.Collections;

public class MangaListProvider implements BaseResponseProvider.IGetData<MangaListResponse> {

    private IGetMangaList caller;
    private MangaListResponseProvider responseProvider;

    public MangaListProvider(IGetMangaList caller) {
        this.caller = caller;
        responseProvider = new MangaListResponseProvider(this);
    }

    public void callData() {
        responseProvider.makeCall();
    }

    public void cancelDataRequest() {
        responseProvider.stopRequest();
    }

    @Override
    public void onGetData(MangaListResponse response) {
        ArrayList<Manga> list = response.getMangas();
        Collections.sort(list);
        caller.onGetData(list);
    }

    @Override
    public void onError(Throwable t) {
        caller.onError(t);
    }


    public interface IGetMangaList {
        void onGetData(ArrayList<Manga> mangaList);

        void onError(Throwable t);
    }
}

package com.amperas17.mangaedenapp.data;


import android.content.Context;

import com.amperas17.mangaedenapp.api.responseprovider.BaseResponseProvider;
import com.amperas17.mangaedenapp.api.responseprovider.MangaFullResponseProvider;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;

public class MangaFullRepository implements BaseResponseProvider.IGetData<MangaFullInfo> {

    private Context context;
    private MangaFullResponseProvider responseProvider;

    public MangaFullRepository(Context context) {
        this.context = context;
        responseProvider = new MangaFullResponseProvider(this);
    }

    public void callData(String... args) {
        responseProvider.makeCall(args);
    }

    public void cancelDataRequest() {
        responseProvider.stopRequest();
    }

    @Override
    public void onGetData(MangaFullInfo mangaFullInfo) {
        ((IGetMangaFull) context).onGetData(mangaFullInfo);
    }

    @Override
    public void onError(Throwable t) {
        ((IGetMangaFull) context).onError(t);
    }


    public interface IGetMangaFull {
        void onGetData(MangaFullInfo mangaFullInfo);

        void onError(Throwable t);
    }
}
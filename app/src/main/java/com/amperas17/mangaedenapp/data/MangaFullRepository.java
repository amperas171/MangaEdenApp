package com.amperas17.mangaedenapp.data;

import com.amperas17.mangaedenapp.api.responseprovider.BaseResponseProvider;
import com.amperas17.mangaedenapp.api.responseprovider.MangaFullResponseProvider;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;


public class MangaFullRepository implements BaseResponseProvider.IGetData<MangaFullInfo> {

    private IGetMangaFull caller;
    private MangaFullResponseProvider responseProvider;

    public MangaFullRepository(IGetMangaFull caller) {
        this.caller = caller;
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
        caller.onGetData(mangaFullInfo);
    }

    @Override
    public void onError(Throwable t) {
        caller.onError(t);
    }


    public interface IGetMangaFull {
        void onGetData(MangaFullInfo mangaFullInfo);

        void onError(Throwable t);
    }
}
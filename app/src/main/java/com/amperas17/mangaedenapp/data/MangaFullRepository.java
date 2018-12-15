package com.amperas17.mangaedenapp.data;

import com.amperas17.mangaedenapp.api.responseprovider.MangaFullResponseProvider;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.utils.Caller;


public class MangaFullRepository implements Caller<MangaFullInfo> {

    private Caller<MangaFullInfo> caller;
    private MangaFullResponseProvider responseProvider;

    public MangaFullRepository(Caller<MangaFullInfo> caller) {
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
}
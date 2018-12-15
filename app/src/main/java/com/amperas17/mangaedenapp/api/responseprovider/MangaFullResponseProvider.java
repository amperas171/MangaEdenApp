package com.amperas17.mangaedenapp.api.responseprovider;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.utils.Caller;

import retrofit2.Call;


public class MangaFullResponseProvider extends BaseResponseProvider<MangaFullInfo, String> {
    public MangaFullResponseProvider(Caller<MangaFullInfo> caller) {
        super(caller);
    }

    @Override
    public Call<MangaFullInfo> initCall(String... args) {
        return MangaEdenApp.getMangaApi().getMangaFullInfo(args[0]);
    }

}

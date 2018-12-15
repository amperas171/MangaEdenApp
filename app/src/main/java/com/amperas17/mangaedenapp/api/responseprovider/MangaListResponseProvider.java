package com.amperas17.mangaedenapp.api.responseprovider;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;
import com.amperas17.mangaedenapp.utils.Caller;

import retrofit2.Call;

public class MangaListResponseProvider extends BaseResponseProvider<MangaListResponse, Integer> {

    public MangaListResponseProvider(Caller<MangaListResponse> caller) {
        super(caller);
    }

    @Override
    public Call<MangaListResponse> initCall(Integer... args) {
        return MangaEdenApp.getMangaApi().getMangaList(MangaApiHelper.ENGLISH, 0);
    }
}

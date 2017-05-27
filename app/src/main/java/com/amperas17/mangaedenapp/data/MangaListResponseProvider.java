package com.amperas17.mangaedenapp.data;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;

import retrofit2.Call;

public class MangaListResponseProvider extends BaseResponseProvider<MangaListResponse> {

    public MangaListResponseProvider(IGetData<MangaListResponse> caller) {
        super(caller);
    }

    @Override
    public Call<MangaListResponse> initCall() {
        return MangaEdenApp.getMangaApi().getMangaList(MangaApiHelper.ENGLISH,0);
    }
}

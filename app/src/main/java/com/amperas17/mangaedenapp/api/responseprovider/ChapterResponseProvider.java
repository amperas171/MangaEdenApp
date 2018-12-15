package com.amperas17.mangaedenapp.api.responseprovider;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.model.page.PageListResponse;
import com.amperas17.mangaedenapp.utils.Caller;

import retrofit2.Call;


public class ChapterResponseProvider extends BaseResponseProvider<PageListResponse, String> {
    public ChapterResponseProvider(Caller<PageListResponse> caller) {
        super(caller);
    }

    @Override
    public Call<PageListResponse> initCall(String... args) {
        return MangaEdenApp.getMangaApi().getPageList(args[0]);
    }
}
package com.amperas17.mangaedenapp.api;

import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.model.page.PageListResponse;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MangaApi {

    @GET("api/list/{lang}/")
    Call<MangaListResponse> getMangaList(@Path("lang") int lang);

    @GET("api/list/{lang}/")
    Call<MangaListResponse> getMangaList(@Path("lang") int lang, @Query("p") int pageNumber);

    @GET("api/list/{lang}/")
    Call<MangaListResponse> getMangaList(@Path("lang") int lang, @Query("p") int pageNumber, @Query("l") int pageSize);


    @GET("api/manga/{mangaId}/")
    Call<MangaFullInfo> getMangaFullInfo(@Path("mangaId") String mangaId);


    @GET("api/chapter/{chapterId}/")
    Call<PageListResponse> getPageList(@Path("chapterId") String chapterId);
}

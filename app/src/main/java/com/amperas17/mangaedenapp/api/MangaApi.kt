package com.amperas17.mangaedenapp.api

import com.amperas17.mangaedenapp.model.manga.MangaFullInfo
import com.amperas17.mangaedenapp.model.page.PageListResponse
import com.amperas17.mangaedenapp.model.manga.MangaListResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaApi {

    @GET("api/list/{lang}/")
    fun getMangaList(@Path("lang") lang: Int): Call<MangaListResponse>

    @GET("api/list/{lang}/")
    fun getMangaList(@Path("lang") lang: Int, @Query("p") pageNumber: Int): Call<MangaListResponse>

    @GET("api/list/{lang}/")
    fun getMangaList(@Path("lang") lang: Int, @Query("p") pageNumber: Int, @Query("l") pageSize: Int): Call<MangaListResponse>


    @GET("api/manga/{mangaId}/")
    fun getMangaFullInfo(@Path("mangaId") mangaId: String): Call<MangaFullInfo>


    @GET("api/chapter/{chapterId}/")
    fun getPageList(@Path("chapterId") chapterId: String): Call<PageListResponse>
}

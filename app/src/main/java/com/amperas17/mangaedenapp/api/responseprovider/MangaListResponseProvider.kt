package com.amperas17.mangaedenapp.api.responseprovider

import com.amperas17.mangaedenapp.api.MangaApiHelper
import com.amperas17.mangaedenapp.model.manga.MangaListResponse
import com.amperas17.mangaedenapp.utils.Caller

import retrofit2.Call


class MangaListResponseProvider(caller: Caller<MangaListResponse>) : BaseResponseProvider<MangaListResponse, String>(caller) {

    override fun initCall(vararg args: String): Call<MangaListResponse> {
        return mangaApi.getMangaList(MangaApiHelper.ENGLISH, 0)
    }
}

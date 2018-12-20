package com.amperas17.mangaedenapp.api.responseprovider

import com.amperas17.mangaedenapp.MangaEdenApp
import com.amperas17.mangaedenapp.model.page.PageListResponse
import com.amperas17.mangaedenapp.utils.Caller

import retrofit2.Call


class ChapterResponseProvider(caller: Caller<PageListResponse>) : BaseResponseProvider<PageListResponse, String>(caller) {

    override fun initCall(vararg args: String): Call<PageListResponse> {
        return MangaEdenApp.mangaApi.getPageList(args[0])
    }
}
package com.amperas17.mangaedenapp.data

import com.amperas17.mangaedenapp.api.responseprovider.MangaListResponseProvider
import com.amperas17.mangaedenapp.model.manga.Manga
import com.amperas17.mangaedenapp.model.manga.MangaListResponse
import com.amperas17.mangaedenapp.utils.Caller

import java.util.ArrayList
import java.util.Collections


class MangaListRepository(private val caller: Caller<ArrayList<Manga>>) : Caller<MangaListResponse> {

    private val responseProvider: MangaListResponseProvider = MangaListResponseProvider(this)

    fun callData() {
        responseProvider.makeCall()
    }

    fun cancelDataRequest() {
        responseProvider.stopRequest()
    }

    override fun onGetData(data: MangaListResponse) {
        val list = data.mangas
        list?.sort()
        list?.let { caller.onGetData(it) }
    }

    override fun onError(t: Throwable) {
        caller.onError(t)
    }
}

package com.amperas17.mangaedenapp.api.responseprovider

import com.amperas17.mangaedenapp.MangaEdenApp
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo
import com.amperas17.mangaedenapp.utils.Caller

import retrofit2.Call


class MangaFullResponseProvider(caller: Caller<MangaFullInfo>) : BaseResponseProvider<MangaFullInfo, String>(caller) {

    override fun initCall(vararg args: String): Call<MangaFullInfo> {
        return MangaEdenApp.mangaApi.getMangaFullInfo(args[0])
    }

}

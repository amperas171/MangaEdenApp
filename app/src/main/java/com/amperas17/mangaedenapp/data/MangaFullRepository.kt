package com.amperas17.mangaedenapp.data

import com.amperas17.mangaedenapp.api.responseprovider.MangaFullResponseProvider
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo
import com.amperas17.mangaedenapp.utils.Caller


class MangaFullRepository(private val caller: Caller<MangaFullInfo>) : Caller<MangaFullInfo> {

    private val responseProvider: MangaFullResponseProvider = MangaFullResponseProvider(this)

    fun callData(vararg args: String) {
        responseProvider.makeCall(*args)
    }

    fun cancelDataRequest() {
        responseProvider.stopRequest()
    }

    override fun onGetData(data: MangaFullInfo) {
        caller.onGetData(data)
    }

    override fun onError(t: Throwable) {
        caller.onError(t)
    }
}
package com.amperas17.mangaedenapp.data

import com.amperas17.mangaedenapp.api.responseprovider.ChapterResponseProvider
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.model.page.PageListResponse
import com.amperas17.mangaedenapp.utils.Caller

import java.util.ArrayList


class ChapterRepository(private val caller: Caller<ArrayList<Page>>) : Caller<PageListResponse> {

    private val responseProvider: ChapterResponseProvider = ChapterResponseProvider(this)

    fun callData(vararg args: String) {
        responseProvider.makeCall(*args)
    }

    fun cancelDataRequest() {
        responseProvider.stopRequest()
    }

    override fun onGetData(data: PageListResponse) {
        val p = data.pages
        p?.let { caller.onGetData(p) }
    }

    override fun onError(t: Throwable) {
        caller.onError(t)
    }
}

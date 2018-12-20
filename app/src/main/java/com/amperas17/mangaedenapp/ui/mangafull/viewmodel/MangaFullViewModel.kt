package com.amperas17.mangaedenapp.ui.mangafull.viewmodel

import com.amperas17.mangaedenapp.data.MangaFullRepository
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo
import com.amperas17.mangaedenapp.ui.BaseViewModel


class MangaFullViewModel : BaseViewModel<MangaFullInfo>() {

    private val repository = MangaFullRepository(this)

    private fun loadData(mangaID: String) {
        repository.callData(mangaID)
    }

    fun startLoading(mangaID: String) {
        super.startLoading()
        loadData(mangaID)
    }

    override fun stopLoading() {
        super.stopLoading()
        repository.cancelDataRequest()
    }
}

package com.amperas17.mangaedenapp.ui.gallery.viewmodel

import com.amperas17.mangaedenapp.data.ChapterRepository
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.ui.BaseViewModel

import java.util.ArrayList


class ChapterPagesViewModel : BaseViewModel<ArrayList<Page>>() {

    private val repository = ChapterRepository(this)

    private fun loadData(chapterID: String) {
        repository.callData(chapterID)
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

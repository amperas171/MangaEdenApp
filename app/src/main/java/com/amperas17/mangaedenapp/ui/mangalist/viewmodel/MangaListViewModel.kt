package com.amperas17.mangaedenapp.ui.mangalist.viewmodel

import com.amperas17.mangaedenapp.data.MangaListRepository
import com.amperas17.mangaedenapp.model.manga.Manga
import com.amperas17.mangaedenapp.ui.BaseViewModel


import java.util.ArrayList

class MangaListViewModel : BaseViewModel<ArrayList<Manga>>(), FindHandler.Caller {

    private val repository: MangaListRepository = MangaListRepository(this)
    private val handler: FindHandler = FindHandler(this)

    private val mangaListAll = arrayListOf<Manga>()

    private fun loadData() {
        repository.callData()
    }

    override fun startLoading() {
        super.startLoading()
        loadData()
    }

    override fun stopLoading() {
        super.stopLoading()
        repository.cancelDataRequest()
    }

    override fun onGetData(data: ArrayList<Manga>) {
        updateMangaListAll(data)
        super.onGetData(data)
    }

    private fun updateMangaListAll(mangaList: ArrayList<Manga>) {
        mangaListAll.clear()
        mangaListAll.addAll(mangaList)
    }


    fun findData(pattern: String) {
        if (mangaListAll.isNotEmpty()) {
            if (pattern.isEmpty()) {
                postSearchResult(mangaListAll)
            } else {
                runSearchInNewThread(pattern)
                isLoading = true
            }
        }
    }

    override fun postSearchResult(mangaList: ArrayList<Manga>) {
        super.onGetData(mangaList)
    }

    private fun runSearchInNewThread(pattern: String) {

        val runnable = Runnable {
            handler.setResultList(mangaListAll.findForMatchesInTitle(pattern))
            handler.sendEmptyMessage(0)
        }

        val thread = Thread(runnable)
        thread.start()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeMessages(0)
    }
}

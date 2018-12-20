package com.amperas17.mangaedenapp.ui.mangalist.viewmodel

import android.os.Handler
import android.os.Message

import com.amperas17.mangaedenapp.model.manga.Manga

import java.lang.ref.WeakReference
import java.util.ArrayList

class FindHandler internal constructor(caller: Caller) : Handler() {

    private val callerRef: WeakReference<Caller> = WeakReference(caller)
    private val resultList = arrayListOf<Manga>()

    internal fun setResultList(list: List<Manga>) {
        resultList.clear()
        resultList.addAll(list)
    }

    override fun handleMessage(msg: Message) {
        callerRef.get()?.postSearchResult(resultList)
    }

    internal interface Caller {
        fun postSearchResult(mangaList: ArrayList<Manga>)
    }
}
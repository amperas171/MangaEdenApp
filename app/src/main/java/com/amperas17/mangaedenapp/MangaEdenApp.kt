package com.amperas17.mangaedenapp

import android.app.Application
import com.amperas17.mangaedenapp.di.apiModule
import org.koin.android.ext.android.startKoin


class MangaEdenApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(apiModule))
    }
}

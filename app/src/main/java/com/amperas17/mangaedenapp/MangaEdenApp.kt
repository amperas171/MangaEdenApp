package com.amperas17.mangaedenapp

import android.app.Application

import com.amperas17.mangaedenapp.api.MangaApi
import com.amperas17.mangaedenapp.model.chapter.Chapter
import com.amperas17.mangaedenapp.model.chapter.ChapterDeserializer
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.model.page.PageDeserializer
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MangaEdenApp : Application() {

    companion object {
        lateinit var mangaApi: MangaApi
            private set
    }

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
    }

    private fun configureRetrofit() {
        val gson = GsonBuilder()
                .registerTypeAdapter(Page::class.java, PageDeserializer())
                .registerTypeAdapter(Chapter::class.java, ChapterDeserializer())
                .create()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.mangaeden.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        mangaApi = retrofit.create(MangaApi::class.java)
    }
}

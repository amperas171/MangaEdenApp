package com.amperas17.mangaedenapp.di

import com.amperas17.mangaedenapp.api.MangaApi
import com.amperas17.mangaedenapp.model.chapter.Chapter
import com.amperas17.mangaedenapp.model.chapter.ChapterDeserializer
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.model.page.PageDeserializer
import com.google.gson.Gson

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val apiModule = module {
    single { createGson() }
    single { createOkHttpClient() }
    single { createMangaApi(get(), get()) }
}

fun createGson(): Gson {
    return GsonBuilder()
            .registerTypeAdapter(Page::class.java, PageDeserializer())
            .registerTypeAdapter(Chapter::class.java, ChapterDeserializer())
            .create()
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor).build()
}

fun createMangaApi(gson: Gson, okHttpClient: OkHttpClient): MangaApi {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://www.mangaeden.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    return retrofit.create(MangaApi::class.java)
}
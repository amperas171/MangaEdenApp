package com.amperas17.mangaedenapp;

import android.app.Application;

import com.amperas17.mangaedenapp.api.MangaApi;
import com.amperas17.mangaedenapp.model.chapter.Chapter;
import com.amperas17.mangaedenapp.model.chapter.ChapterDeserializer;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MangaEdenApp extends Application {
    private static MangaApi mangaApi;
    private Retrofit retrofit;

    public static MangaApi getMangaApi(){
        return mangaApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        configureRetrofit();
    }

    private void configureRetrofit() {
        //Конвертор
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Page.class, new PageDeserializer())
                .registerTypeAdapter(Chapter.class, new ChapterDeserializer())
                .create();

        //Логирование
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.mangaeden.com/") //Базовая часть адреса
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();

        mangaApi = retrofit.create(MangaApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }
}

package com.amperas17.mangaedenapp.data;


import android.content.Context;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MangaListProviderOld {

    private Context context;
    private Call<MangaListResponse> mangaListRequestCall;
    //private List<Manga> mangaList;

    public MangaListProviderOld(Context context){
        this.context = context;
        mangaListRequestCall = MangaEdenApp.getMangaApi().getMangaList(MangaApiHelper.ENGLISH,0);
        //mangaList = new ArrayList<>();
    }

    public void getMangaList() {
        mangaListRequestCall.enqueue(new Callback<MangaListResponse>() {
            @Override
            public void onFailure(Call<MangaListResponse> call, Throwable t) {
                ((IGetData)context).onError(t);
            }

            @Override
            public void onResponse(Call<MangaListResponse> call, Response<MangaListResponse> response) {
                List<Manga> mangaList = new ArrayList<>();
                //mangaListAll.addAll(response.body().getMangas().subList(ZERO,MANGA_LIST_ALL_NUMBER));
                mangaList.addAll(response.body().getMangas());
                Collections.sort(mangaList);
                //addDataToAdapter(mangaList.subList(ZERO, MANGA_LIST_NUMBER));
                //stopRefresh();
                ((IGetData)context).onGetDate(mangaList);
            }
        });
    }

    public void stopRequest(){
        mangaListRequestCall.cancel();


    }

    public interface IGetData{
        public void onGetDate(List<Manga> mangaList);
        public void onError(Throwable t);
    }

}

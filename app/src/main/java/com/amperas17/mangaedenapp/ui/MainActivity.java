package com.amperas17.mangaedenapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApi;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Button btnGetMangas, btnGetPages, btnGetMangasSync, btnGetMangaFullInfo;
    public TextView tvMangasNumber;
    private MangaApi mangaApi = MangaEdenApp.getMangaApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMangasNumber = (TextView) findViewById(R.id.tvMangasNumber);

        btnGetMangas = (Button) findViewById(R.id.btnGetMangas);
        btnGetMangas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaApi.getMangaList(0,0,25).enqueue(new Callback<MangaListResponse>() {
                    @Override
                    public void onFailure(Call<MangaListResponse> call, Throwable t) {
                        Log.d("mmanga", "onFailure: " + t.getMessage());
                    }

                    @Override
                    public void onResponse(Call<MangaListResponse> call, Response<MangaListResponse> response) {
                        Log.d("mmanga", "onResponse: " + response.body().toString());
                        Log.d("mmanga", "onResponse: " + response.body().getMangas().size());
                    }
                });
            }
        });

        btnGetMangaFullInfo = (Button) findViewById(R.id.btnGetMangaFullInfo);
        btnGetMangaFullInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaApi.getMangaFullInfo("54430be945b9ef3a6d5818cc").enqueue(new Callback<MangaFullInfo>() {
                    @Override
                    public void onResponse(Call<MangaFullInfo> call, Response<MangaFullInfo> response) {
                        Log.d("mmanga", "onResponse: " + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<MangaFullInfo> call, Throwable t) {
                        Log.d("mmanga", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        btnGetPages = (Button) findViewById(R.id.btnGetPages);
        btnGetPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mangaApi.getPageList("4e711cb0c09225616d037cc2").enqueue(new Callback<PageListResponse>() {
                    @Override
                    public void onFailure(Call<PageListResponse> call, Throwable t) {
                        Log.d("mmanga", "onFailure: " + t.getMessage());
                    }

                    @Override
                    public void onResponse(Call<PageListResponse> call, Response<PageListResponse> response) {
                        Log.d("mmanga", "onResponse: ");
                        for (Page page : response.body().getPages()) {
                            Log.d("mmanga", page.toString());
                        }
                    }
                });
            }
        });

        /*btnGetMangasSync = (Button) findViewById(R.id.btnGetMangasSync);
        btnGetMangasSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MangaListResponse response = MangaEdenApp.getMangaApi().getMangaList(0).execute().body();
                            //Log.d("mmanga", response.getMangas().toString());
                            for (Manga manga : response.getMangas()){
                                Log.d("mmanga", manga.getLastChapterDate() + " " + new Date(manga.getLastChapterDate()*1000).toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });*/

        btnGetMangaFullInfo.callOnClick();

    }
}

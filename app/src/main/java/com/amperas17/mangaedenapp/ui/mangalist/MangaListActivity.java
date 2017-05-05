package com.amperas17.mangaedenapp.ui.mangalist;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApi;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.model.manga.MangaListResponse;
import com.amperas17.mangaedenapp.ui.mangafull.MangaFullActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String MANGA_LIST_TAG = "mangaList";
    public static final String MANGA_LIST_ALL_TAG = "mangaListAll";

    public static final String IS_UPDATING_TAG = "isUpdating";
    public static final String IS_LOADING_TAG = "isLoading";
    public static final String SEARCH_TAG = "searchText";
    public static final String IS_SEARCHING_TAG = "isSearching";


    public static final int ZERO = 0;

    public static final int MANGA_LIST_NUMBER = 50;
    public static final int MANGA_LIST_ALL_NUMBER = 2000;


    private MangaApi mangaApi;
    private Call<MangaListResponse> mangaListRequestCall;

    ArrayList<Manga> mangaListAll = new ArrayList<>();

    private MangaAdapter mangaAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView tvNoData;
    private SearchView searchView;

    private boolean isUpdating = false;
    private boolean isLoading = true;
    private boolean isSearching = false;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);

        mangaApi = MangaEdenApp.getMangaApi();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isUpdating = true;
                callDataRequest();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mangaAdapter = new MangaAdapter(new MangaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Manga manga) {
                if (manga != null) {
                    startActivity(MangaFullActivity.newIntent(MangaListActivity.this, manga.getID(), manga.getTitle()));
                } else {
                    Toast.makeText(MangaListActivity.this, R.string.incorrect_manga_id, Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMangaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mangaAdapter);

        tvNoData = (TextView) findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            ArrayList<Manga> list = savedInstanceState.getParcelableArrayList(MANGA_LIST_TAG);
            addDataToAdapter(list);

            ArrayList<Manga> listAll = savedInstanceState.getParcelableArrayList(MANGA_LIST_ALL_TAG);
            if (mangaListAll != null && listAll != null) {
                mangaListAll.addAll(listAll);
            }

            isSearching = savedInstanceState.getBoolean(IS_SEARCHING_TAG,false);
            if (isSearching) {
                searchText = savedInstanceState.getString(SEARCH_TAG);
            }

            stopRefresh();
            isUpdating = savedInstanceState.getBoolean(IS_UPDATING_TAG);
            if (isUpdating) {
                swipeRefreshLayout.setRefreshing(true);
                callDataRequest();
            }

            isLoading = savedInstanceState.getBoolean(IS_LOADING_TAG);
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                callDataRequest();
            }
        } else {
            callDataRequest();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MANGA_LIST_TAG, mangaAdapter.mangaList);
        outState.putParcelableArrayList(MANGA_LIST_ALL_TAG, mangaListAll);
        outState.putBoolean(IS_UPDATING_TAG, isUpdating);
        outState.putBoolean(IS_LOADING_TAG, isLoading);
        outState.putString(SEARCH_TAG,searchText);
        outState.putBoolean(IS_SEARCHING_TAG, !searchView.isIconified());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mangaListRequestCall != null) {
            mangaListRequestCall.cancel();
        }
    }

    private void callDataRequest() {
        tvNoData.setVisibility(View.GONE);
        mangaListRequestCall = mangaApi.getMangaList(MangaApi.ENGLISH);
        mangaListRequestCall.enqueue(new Callback<MangaListResponse>() {
            @Override
            public void onFailure(Call<MangaListResponse> call, Throwable t) {
                stopRefresh();
                if (!call.isCanceled()) {
                    tvNoData.setText("Error occurred: " + t.getMessage() + ". Check internet connection and try again.");
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(Call<MangaListResponse> call, Response<MangaListResponse> response) {
                mangaListAll.clear();
                //mangaListAll.addAll(response.body().getMangas().subList(ZERO,MANGA_LIST_ALL_NUMBER));
                mangaListAll.addAll(response.body().getMangas());
                Collections.sort(mangaListAll);
                addDataToAdapter(mangaListAll.subList(ZERO, MANGA_LIST_NUMBER));
                stopRefresh();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manga_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        if (isSearching){
            menuItem.expandActionView();
            searchView.setQuery(searchText, true);
            searchView.setIconified(false);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        tvNoData.setVisibility(View.GONE);
        searchText = newText;
        if (!mangaListAll.isEmpty()) {
            if (newText.isEmpty()) {
                addDataToAdapter(mangaListAll.subList(ZERO, MANGA_LIST_NUMBER));
            } else {
                List<Manga> resultList = new ArrayList<>();
                for (Manga manga : mangaListAll) {
                    if (manga.getTitle().toLowerCase().startsWith(newText.toLowerCase())
                            || manga.getTitle().toLowerCase().contains(" " + newText.toLowerCase())) {
                        resultList.add(manga);
                    }
                }
                if (!resultList.isEmpty()) {
                    Collections.sort(resultList, new Comparator<Manga>() {
                        @Override
                        public int compare(Manga manga1, Manga manga2) {
                            return manga2.getHits() - manga1.getHits();
                        }
                    });
                } else {
                    tvNoData.setText("There is no match");
                    tvNoData.setVisibility(View.VISIBLE);
                }
                addDataToAdapter(resultList);
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (swipeRefreshLayout.isRefreshing()){
            mangaListRequestCall.cancel();
        } else if (!searchView.isIconified()){
            addDataToAdapter(mangaListAll.subList(ZERO, MANGA_LIST_NUMBER));
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    private void addDataToAdapter(List<Manga> mangas) {
        mangaAdapter.mangaList.clear();
        mangaAdapter.mangaList.addAll(mangas);
        mangaAdapter.notifyDataSetChanged();
    }

    private void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        isUpdating = false;
        isLoading = false;
    }

}

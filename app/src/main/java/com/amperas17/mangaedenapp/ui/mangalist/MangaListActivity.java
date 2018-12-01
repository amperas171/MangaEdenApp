package com.amperas17.mangaedenapp.ui.mangalist;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.data.MangaListProvider;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.ui.mangafull.MangaFullActivity;
import com.amperas17.mangaedenapp.utils.adapter.AdapterItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MangaListActivity extends AppCompatActivity implements MangaListProvider.IGetMangaList {

    public static final String MANGA_LIST_TAG = "mangaList";
    public static final String MANGA_LIST_ALL_TAG = "mangaListAll";

    public static final String IS_UPDATING_TAG = "isUpdating";
    public static final String IS_LOADING_TAG = "isLoading";
    public static final String SEARCH_TAG = "searchText";
    public static final String IS_SEARCHING_TAG = "isSearching";


    private MangaListProvider mangaListProvider;

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

        mangaListProvider = new MangaListProvider(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isUpdating = true;
                callDataRequest();
            }
        });

        progressBar = findViewById(R.id.progressBar);

        mangaAdapter = new MangaAdapter(new AdapterItemClickListener<Manga>() {
            @Override
            public void onItemClick(Manga manga) {
                if (manga != null) {
                    startActivity(MangaFullActivity.newIntent(MangaListActivity.this, manga.getID(), manga.getTitle()));
                } else {
                    Toast.makeText(MangaListActivity.this, R.string.incorrect_manga_id, Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMangaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mangaAdapter);

        tvNoData = findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            ArrayList<Manga> list = savedInstanceState.getParcelableArrayList(MANGA_LIST_TAG);
            addDataToAdapter(list);

            ArrayList<Manga> listAll = savedInstanceState.getParcelableArrayList(MANGA_LIST_ALL_TAG);
            if (mangaListAll != null && listAll != null) {
                mangaListAll.addAll(listAll);
            }

            isSearching = savedInstanceState.getBoolean(IS_SEARCHING_TAG, false);
            if (isSearching) {
                searchText = savedInstanceState.getString(SEARCH_TAG);
                swipeRefreshLayout.setEnabled(!isSearching);
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
        outState.putString(SEARCH_TAG, searchText);
        outState.putBoolean(IS_SEARCHING_TAG, !searchView.isIconified());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mangaListProvider.cancelDataRequest();
    }

    private void callDataRequest() {
        tvNoData.setVisibility(View.GONE);
        mangaListProvider.callData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manga_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        if (isSearching) {
            menuItem.expandActionView();
            searchView.setQuery(searchText, true);
            searchView.setIconified(false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextChanged(newText);
                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setEnabled(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                swipeRefreshLayout.setEnabled(true);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            addDataToAdapter(mangaListAll);
            searchView.onActionViewCollapsed();
            swipeRefreshLayout.setEnabled(true);
        } else if (swipeRefreshLayout.isRefreshing()) {
            mangaListProvider.cancelDataRequest();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onGetData(ArrayList<Manga> mangaList) {
        this.mangaListAll = mangaList;
        addDataToAdapter(mangaList);
        stopRefresh();
    }

    @Override
    public void onError(Throwable t) {
        tvNoData.setText("Error occurred: " + t.getMessage() + ". Check internet connection and try again.");
        tvNoData.setVisibility(View.VISIBLE);
        mangaAdapter.mangaList.clear();
        mangaAdapter.notifyDataSetChanged();
        stopRefresh();
    }

    private void onQueryTextChanged(String pattern) {
        tvNoData.setVisibility(View.GONE);
        searchText = pattern;
        if (!mangaListAll.isEmpty()) {
            if (pattern.isEmpty()) {
                addDataToAdapter(mangaListAll);
            } else {
                runSearchInNewThread(pattern);
            }
        }
    }

    private void runSearchInNewThread(final String pattern) {

        final List<Manga> resultList = new ArrayList<>();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (resultList.isEmpty()) {
                    tvNoData.setText("There is no match");
                    tvNoData.setVisibility(View.VISIBLE);
                }
                addDataToAdapter(resultList);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                resultList.addAll(findForMatchesInTitle(pattern));
                handler.sendEmptyMessage(0);
            }
        };

        swipeRefreshLayout.setRefreshing(true);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private ArrayList<Manga> findForMatchesInTitle(String pattern) {
        ArrayList<Manga> resultList = new ArrayList<>();
        for (Manga manga : mangaListAll) {
            if (manga.getTitle().toLowerCase().startsWith(pattern.toLowerCase())
                    || manga.getTitle().toLowerCase().contains(" " + pattern.toLowerCase())) {
                resultList.add(manga);
            }
        }
        Collections.sort(resultList, new Comparator<Manga>() {
            @Override
            public int compare(Manga manga1, Manga manga2) {
                return manga2.getHits() - manga1.getHits();
            }
        });
        return resultList;
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

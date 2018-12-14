package com.amperas17.mangaedenapp.ui.mangalist.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
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
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.ui.mangafull.MangaFullActivity;
import com.amperas17.mangaedenapp.ui.mangalist.model.MangaListResource;
import com.amperas17.mangaedenapp.ui.mangalist.viewmodel.MangaListViewModel;
import com.amperas17.mangaedenapp.utils.adapter.AdapterItemClickListener;

import java.util.ArrayList;


public class MangaListActivity extends AppCompatActivity {

    public static final String MANGA_LIST_TAG = "mangaList";

    public static final String IS_UPDATING_TAG = "isUpdating";
    public static final String IS_LOADING_TAG = "isLoading";
    public static final String SEARCH_TAG = "searchText";
    public static final String IS_SEARCHING_TAG = "isSearching";

    private MangaListViewModel viewModel;
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

        progressBar = findViewById(R.id.progressBar);
        tvNoData = findViewById(R.id.tvNoData);

        initAdapter();
        initSwipeRefreshLayout();
        initViewModel();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            callDataRequest();
        }
    }

    private void initAdapter() {
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
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isUpdating = true;
                callDataRequest();
            }
        });
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MangaListViewModel.class);
        viewModel.getResource().observe(this, new Observer<MangaListResource>() {
            @Override
            public void onChanged(@Nullable MangaListResource resource) {
                onResourceChanged(resource);
            }
        });
    }

    private void onResourceChanged(MangaListResource resource) {
        if (resource != null) {
            if (resource.getMangas() != null)
                onGetData(resource.getMangas());
            if (resource.getThrowable() != null)
                onError(resource.getThrowable());
        }
    }

    private void onGetData(ArrayList<Manga> mangaList) {
        addDataToAdapter(mangaList);
        stopRefresh();
        if (mangaList.isEmpty()) {
            tvNoData.setText(R.string.no_match);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void onError(Throwable t) {
        if (mangaAdapter.getMangaList().isEmpty()) {
            tvNoData.setText(getString(R.string.error, t.getMessage()));
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getString(R.string.error, t.getMessage()), Toast.LENGTH_LONG).show();
        }
        stopRefresh();
    }

    private void restoreState(Bundle savedInstanceState) {
        ArrayList<Manga> list = savedInstanceState.getParcelableArrayList(MANGA_LIST_TAG);
        addDataToAdapter(list);

        isSearching = savedInstanceState.getBoolean(IS_SEARCHING_TAG, false);
        if (isSearching) {
            searchText = savedInstanceState.getString(SEARCH_TAG);
            swipeRefreshLayout.setEnabled(false);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MANGA_LIST_TAG, mangaAdapter.getMangaList());
        outState.putBoolean(IS_UPDATING_TAG, isUpdating);
        outState.putBoolean(IS_LOADING_TAG, isLoading);
        outState.putString(SEARCH_TAG, searchText);
        outState.putBoolean(IS_SEARCHING_TAG, !searchView.isIconified());
    }


    private void callDataRequest() {
        tvNoData.setVisibility(View.GONE);
        viewModel.startLoading();
    }

    private void addDataToAdapter(ArrayList<Manga> mangas) {
        mangaAdapter.getMangaList().clear();
        mangaAdapter.getMangaList().addAll(mangas);
        mangaAdapter.notifyDataSetChanged();
    }

    private void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        isUpdating = false;
        isLoading = false;
    }


    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            swipeRefreshLayout.setEnabled(true);
        } else if (swipeRefreshLayout.isRefreshing()) {
            viewModel.stopLoading();
            stopRefresh();
        } else {
            super.onBackPressed();
        }
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

    private void onQueryTextChanged(String pattern) {
        tvNoData.setVisibility(View.GONE);
        searchText = pattern;
        viewModel.findData(pattern);
    }
}

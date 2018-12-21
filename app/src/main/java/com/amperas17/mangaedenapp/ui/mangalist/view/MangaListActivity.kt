package com.amperas17.mangaedenapp.ui.mangalist.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.model.manga.Manga
import com.amperas17.mangaedenapp.ui.mangafull.view.MangaFullActivity
import com.amperas17.mangaedenapp.model.Resource
import com.amperas17.mangaedenapp.ui.mangalist.viewmodel.MangaListViewModel
import com.amperas17.mangaedenapp.utils.AdapterItemClickListener
import kotlinx.android.synthetic.main.activity_manga_list.*
import org.koin.android.viewmodel.ext.android.viewModel

import java.util.ArrayList


class MangaListActivity : AppCompatActivity() {

    companion object {

        const val MANGA_LIST_TAG = "mangaList"

        const val IS_UPDATING_TAG = "isUpdating"
        const val IS_LOADING_TAG = "isLoading"
        const val SEARCH_TAG = "searchText"
        const val IS_SEARCHING_TAG = "isSearching"
    }

    private val viewModel by viewModel<MangaListViewModel>()
    private lateinit var mangaAdapter: MangaAdapter

    private lateinit var searchView: SearchView

    private var isUpdating = false
    private var isLoading = true
    private var isSearching = false
    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)

        initAdapter()
        initSwipeRefreshLayout()
        initViewModel()

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            callDataRequest()
        }
    }

    private fun initAdapter() {
        mangaAdapter = MangaAdapter(object : AdapterItemClickListener<Manga> {
            override fun onItemClick(manga: Manga) {
                startActivity(MangaFullActivity.newIntent(this@MangaListActivity, manga.id ?: "", manga.title ?: ""))
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMangaList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mangaAdapter
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright)
        swipeRefreshLayout.setOnRefreshListener {
            isUpdating = true
            callDataRequest()
        }
    }

    private fun initViewModel() {
        viewModel.getResource().observe(this, Observer { resource -> onResourceChanged(resource) })
    }

    private fun onResourceChanged(resource: Resource<ArrayList<Manga>>?) {
        if (resource != null) {
            if (resource.data != null)
                onGetData(resource.data)
            if (resource.throwable != null)
                onError(resource.throwable)
        }
    }

    private fun onGetData(mangaList: ArrayList<Manga>?) {
        addDataToAdapter(mangaList)
        stopRefresh()
        if (mangaList.isNullOrEmpty()) {
            tvNoData.setText(R.string.no_match)
            tvNoData.visibility = View.VISIBLE
        }
    }

    private fun onError(t: Throwable?) {
        if (mangaAdapter.mangaList.isEmpty()) {
            tvNoData.text = getString(R.string.error, t?.message)
            tvNoData.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, getString(R.string.error, t?.message), Toast.LENGTH_LONG).show()
        }
        stopRefresh()
    }

    private fun restoreState(savedInstanceState: Bundle) {
        val list = savedInstanceState.getParcelableArrayList<Manga>(MANGA_LIST_TAG)
        addDataToAdapter(list)

        isSearching = savedInstanceState.getBoolean(IS_SEARCHING_TAG, false)
        if (isSearching) {
            searchText = savedInstanceState.getString(SEARCH_TAG, "")
            swipeRefreshLayout.isEnabled = false
        }

        stopRefresh()
        isUpdating = savedInstanceState.getBoolean(IS_UPDATING_TAG)
        if (isUpdating) {
            swipeRefreshLayout.isRefreshing = true
            callDataRequest()
        }

        isLoading = savedInstanceState.getBoolean(IS_LOADING_TAG)
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            callDataRequest()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(MANGA_LIST_TAG, mangaAdapter.mangaList)
        outState.putBoolean(IS_UPDATING_TAG, isUpdating)
        outState.putBoolean(IS_LOADING_TAG, isLoading)
        outState.putString(SEARCH_TAG, searchText)
        outState.putBoolean(IS_SEARCHING_TAG, searchView.isIconified)
    }


    private fun callDataRequest() {
        tvNoData.visibility = View.GONE
        viewModel.startLoading()
    }

    private fun addDataToAdapter(mangas: ArrayList<Manga>?) {
        mangas?.let {
            mangaAdapter.mangaList.clear()
            mangaAdapter.mangaList.addAll(mangas)
            mangaAdapter.notifyDataSetChanged()
        }
    }

    private fun stopRefresh() {
        swipeRefreshLayout.isRefreshing = false
        progressBar.visibility = View.GONE
        isUpdating = false
        isLoading = false
    }


    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            swipeRefreshLayout!!.isEnabled = true
        } else if (swipeRefreshLayout.isRefreshing) {
            viewModel.stopLoading()
            stopRefresh()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manga_list, menu)
        val menuItem = menu.findItem(R.id.action_search)
        searchView = menuItem.actionView as SearchView
        if (isSearching) {
            menuItem.expandActionView()
            searchView.setQuery(searchText, true)
            searchView.isIconified = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                onQueryTextChanged(newText)
                return true
            }
        })

        searchView.setOnSearchClickListener { swipeRefreshLayout.isEnabled = false }

        searchView.setOnCloseListener {
            swipeRefreshLayout.isEnabled = true
            false
        }

        return true
    }

    private fun onQueryTextChanged(pattern: String) {
        tvNoData.visibility = View.GONE
        searchText = pattern
        viewModel.findData(pattern)
    }
}

package com.amperas17.mangaedenapp.ui.mangafull.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.api.MangaApiHelper
import com.amperas17.mangaedenapp.model.Resource
import com.amperas17.mangaedenapp.model.chapter.Chapter
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo
import com.amperas17.mangaedenapp.ui.gallery.view.ChapterPagesActivity
import com.amperas17.mangaedenapp.ui.mangafull.viewmodel.MangaFullViewModel
import com.amperas17.mangaedenapp.utils.AdapterItemClickListener
import com.amperas17.mangaedenapp.utils.formatDefaultFromSeconds
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manga_full.*

import java.util.ArrayList


class MangaFullActivity : AppCompatActivity() {

    companion object {

        const val MANGA_ID_TAG = "MangaID"
        const val MANGA_TITLE_TAG = "MangaTitle"

        fun newIntent(context: Context, mangaID: String, mangaTitle: String): Intent {
            val intent = Intent(context, MangaFullActivity::class.java)
            intent.putExtra(MANGA_ID_TAG, mangaID)
            intent.putExtra(MANGA_TITLE_TAG, mangaTitle)
            return intent
        }
    }

    private lateinit var viewModel: MangaFullViewModel
    private lateinit var chapterAdapter: ChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_full)

        val mangaID = intent?.extras?.getString(MANGA_ID_TAG) ?: ""
        val mangaTitle = intent?.extras?.getString(MANGA_TITLE_TAG) ?: ""

        initActionBar(mangaTitle)
        initAdapter()
        initViewModel()

        callDataRequest(mangaID)
    }

    private fun initActionBar(mangaTitle: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mangaTitle
    }

    private fun initAdapter() {
        chapterAdapter = ChapterAdapter(object : AdapterItemClickListener<Chapter> {
            override fun onItemClick(chapter: Chapter) {
                startActivity(ChapterPagesActivity.newIntent(this@MangaFullActivity,
                        chapter.id ?: "", chapter.title ?: ""))
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChapterList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chapterAdapter
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MangaFullViewModel::class.java)
        viewModel.getResource().observe(this, Observer { resource -> onResourceChanged(resource) })
    }

    private fun onResourceChanged(resource: Resource<MangaFullInfo>?) {
        if (resource != null) {
            if (resource.data != null)
                onGetData(resource.data)
            if (resource.throwable != null)
                onError(resource.throwable)
        }
    }

    private fun callDataRequest(mangaID: String) {
        progressBar.visibility = View.VISIBLE
        mangaFullContainer.visibility = View.GONE
        viewModel.startLoading(mangaID)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onGetData(mangaFullInfo: MangaFullInfo?) {
        mangaFullInfo?.let {
            initView(mangaFullInfo)
        }
    }

    private fun onError(t: Throwable?) {
        progressBar.visibility = View.GONE
        Toast.makeText(this@MangaFullActivity, t?.message, Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    override fun onBackPressed() {
        if (progressBar.visibility == View.VISIBLE)
            viewModel.stopLoading()
        super.onBackPressed()
    }

    private fun initView(mangaFullInfo: MangaFullInfo) {

        val image = mangaFullInfo.image
        image?.let {
            Picasso.with(this@MangaFullActivity)
                    .load(MangaApiHelper.buildUrl(image))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(ivFullMangaImage)
        }

        tvAuthor.text = mangaFullInfo.author
        tvHits.text = mangaFullInfo.hits.toString()
        tvReleased.text = mangaFullInfo.last_chapter_date.formatDefaultFromSeconds()
        tvDescription.text = Html.fromHtml(mangaFullInfo.description)
        tvCategories.text = mangaFullInfo.categoriesAsString

        addDataToAdapter(mangaFullInfo.chapters)

        progressBar.visibility = View.GONE
        mangaFullContainer.visibility = View.VISIBLE
    }

    private fun addDataToAdapter(chapters: ArrayList<Chapter>?) {
        chapters?.let {
            chapterAdapter.chapterList.clear()
            chapterAdapter.chapterList.addAll(chapters)
            chapterAdapter.notifyDataSetChanged()
        }
    }
}
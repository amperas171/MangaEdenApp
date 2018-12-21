package com.amperas17.mangaedenapp.ui.gallery.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.model.Resource
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.ui.gallery.viewmodel.ChapterPagesViewModel
import com.amperas17.mangaedenapp.ui.zoominggallery.ZoomImageActivity
import com.amperas17.mangaedenapp.utils.hideStatusBar
import kotlinx.android.synthetic.main.activity_chapter_pages.*
import org.koin.android.viewmodel.ext.android.viewModel

import java.util.ArrayList


class ChapterPagesActivity : AppCompatActivity() {

    companion object {

        const val CHAPTER_ID_TAG = "ChapterID"
        const val CHAPTER_TITLE_TAG = "ChapterTitle"

        const val ZOOM_ACTIVITY_REQUEST_CODE = 101
        const val POSITION_TAG = "position"

        fun newIntent(context: Context, chapterID: String, chapterTitle: String): Intent {
            val intent = Intent(context, ChapterPagesActivity::class.java)
            intent.putExtra(CHAPTER_ID_TAG, chapterID)
            intent.putExtra(CHAPTER_TITLE_TAG, chapterTitle)
            return intent
        }
    }

    private val viewModel by viewModel<ChapterPagesViewModel>()
    private lateinit var galleryAdapter: PageAdapter

    private val pageList = ArrayList<Page>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_pages)
        hideStatusBar()

        val chapterID = intent.extras?.getString(CHAPTER_ID_TAG) ?: ""
        val chapterTitle = intent.extras?.getString(CHAPTER_TITLE_TAG) ?: ""

        supportActionBar?.hide()
        initAdapter()
        initViewModel()

        callDataRequest(chapterID)
    }

    private fun initAdapter() {
        galleryAdapter = PageAdapter(object : PageAdapter.OnItemClickListener {
            override fun onItemClick(page: Page, position: Int) {
                startActivityForResult(
                        ZoomImageActivity.newIntent(this@ChapterPagesActivity, pageList, position),
                        ZOOM_ACTIVITY_REQUEST_CODE
                )
            }
        })

        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = galleryAdapter
    }

    private fun initViewModel() {
        viewModel.getResource().observe(this, Observer { resource -> onResourceChanged(resource) })
    }

    private fun onResourceChanged(resource: Resource<ArrayList<Page>>?) {
        if (resource != null) {
            if (resource.data != null)
                onGetData(resource.data)
            if (resource.throwable != null)
                onError(resource.throwable)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ZOOM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val position = data?.getIntExtra(POSITION_TAG, 0) ?: 0
                recyclerView.scrollToPosition(position)
            }
        }
    }

    private fun callDataRequest(chapterID: String?) {
        viewModel.startLoading(chapterID!!)
    }

    private fun addDataToAdapter(list: ArrayList<Page>) {
        galleryAdapter.pageList.clear()
        galleryAdapter.pageList.addAll(list)
        galleryAdapter.notifyDataSetChanged()
    }

    private fun onGetData(pages: ArrayList<Page>?) {
        pages?.let {
            pageList.addAll(pages)
            addDataToAdapter(pages)
        }
    }

    private fun onError(t: Throwable?) {}
}

package com.amperas17.mangaedenapp.ui.zoominggallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.model.page.Page
import com.amperas17.mangaedenapp.utils.makeFullScreen

import java.util.ArrayList


class ZoomImageActivity : AppCompatActivity() {

    companion object {

        const val IMAGES_TAG = "images"
        const val POSITION_TAG = "position"

        fun newIntent(context: Context, pageList: ArrayList<Page>, position: Int): Intent {
            val intent = Intent(context, ZoomImageActivity::class.java)
            intent.putParcelableArrayListExtra(IMAGES_TAG, pageList)
            intent.putExtra(POSITION_TAG, position)
            return intent
        }
    }

    private lateinit var pager: ZoomViewPager
    private lateinit var pageList: ArrayList<Page>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_zoom)
        initPager()
    }

    override fun onBackPressed() {
        returnResultOnExit()
        super.onBackPressed()
    }

    private fun returnResultOnExit() {
        val intent = Intent()
        intent.putExtra(POSITION_TAG, pager.currentItem)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun initPager() {
        pageList = intent?.extras?.getParcelableArrayList(IMAGES_TAG) ?: arrayListOf()
        val position = intent?.extras?.getInt(POSITION_TAG) ?: 0

        pager = findViewById(R.id.pager)
        val adapter = ZoomPagerAdapter(pageList)
        pager.adapter = adapter
        pager.currentItem = position
    }
}

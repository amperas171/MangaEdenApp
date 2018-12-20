package com.amperas17.mangaedenapp.ui.zoominggallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.api.MangaApiHelper
import com.amperas17.mangaedenapp.model.page.Page
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView

import java.util.ArrayList


class ZoomPagerAdapter(private var pageList: ArrayList<Page>) : PagerAdapter() {

    override fun getCount(): Int {
        return pageList.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = layoutInflater.inflate(R.layout.item_pager_zoom, container, false)

        val photoView = itemView.findViewById<PhotoView>(R.id.photo_view)

        //For very long images
        var weight = Target.SIZE_ORIGINAL
        if (pageList[position].height > 2000) weight = 430

        Glide.with(container.context)
                .load(MangaApiHelper.buildUrl(pageList[position].url ?: ""))
                .error(R.drawable.noimage)
                .override(weight, Target.SIZE_ORIGINAL)
                .into(photoView)

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}

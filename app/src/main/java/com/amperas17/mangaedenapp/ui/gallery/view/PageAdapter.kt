package com.amperas17.mangaedenapp.ui.gallery.view

import android.content.res.Configuration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.api.MangaApiHelper
import com.amperas17.mangaedenapp.model.page.Page
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_gallery.*

import java.util.ArrayList


class PageAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {

    var pageList: ArrayList<Page> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pageList[position], listener)
    }

    override fun getItemCount(): Int {
        return pageList.size
    }


    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(pageItem: Page, listener: OnItemClickListener) {

            var preload = 0.25f
            if (itemView.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                preload = 1f
            }

            val url = pageItem.url
            url?.let {
                Glide.with(itemView.context)
                        .load(MangaApiHelper.buildUrl(url))
                        .thumbnail(preload)
                        .crossFade()
                        .error(R.drawable.noimage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPageImage)
            }

            ivPageImage.setOnClickListener { listener.onItemClick(pageItem, adapterPosition) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(page: Page, position: Int)
    }
}

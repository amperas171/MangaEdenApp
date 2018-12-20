package com.amperas17.mangaedenapp.ui.mangalist.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.api.MangaApiHelper
import com.amperas17.mangaedenapp.model.manga.Manga
import com.amperas17.mangaedenapp.utils.AdapterItemClickListener
import com.amperas17.mangaedenapp.utils.formatDefaultFromSeconds
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_manga.*

import java.util.ArrayList


class MangaAdapter(private val listener: AdapterItemClickListener<Manga>) : RecyclerView.Adapter<MangaAdapter.ViewHolder>() {

    val mangaList: ArrayList<Manga> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_manga, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mangaList[position], listener)
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        internal fun bind(mangaItem: Manga, listener: AdapterItemClickListener<Manga>) {

            val image = mangaItem.image
            image?.let {
                Picasso.with(itemView.context)
                        .load(MangaApiHelper.buildUrl(image))
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivMangaImage)
            }

            tvMangaTitle.text = mangaItem.title
            tvMangaHits.text = mangaItem.hits.toString()
            tvMangaDate.text = mangaItem.lastChapterDate.formatDefaultFromSeconds()

            itemView.setOnClickListener { listener.onItemClick(mangaItem) }
        }
    }

}


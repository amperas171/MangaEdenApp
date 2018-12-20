package com.amperas17.mangaedenapp.ui.mangafull.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amperas17.mangaedenapp.R
import com.amperas17.mangaedenapp.model.chapter.Chapter
import com.amperas17.mangaedenapp.utils.AdapterItemClickListener
import com.amperas17.mangaedenapp.utils.formatDefaultFromSeconds
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chapter.*

import java.util.ArrayList


class ChapterAdapter(private val listener: AdapterItemClickListener<Chapter>) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    var chapterList: ArrayList<Chapter> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chapterList[position], listener)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(chapterItem: Chapter, listener: AdapterItemClickListener<Chapter>) {
            tvMangaTitle.text = chapterItem.number.toString()
            tvChapterTitle.text = chapterItem.title
            tvDate.text = chapterItem.date.formatDefaultFromSeconds()

            itemView.setOnClickListener { listener.onItemClick(chapterItem) }
        }
    }
}

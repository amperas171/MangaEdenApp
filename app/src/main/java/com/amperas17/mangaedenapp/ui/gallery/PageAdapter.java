package com.amperas17.mangaedenapp.ui.gallery;


import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.page.Page;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> {

    ArrayList<Page> pageList;
    private OnItemClickListener listener;

    PageAdapter(OnItemClickListener listener) {
        this.pageList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(pageList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPageImage;

        private ViewHolder(View view) {
            super(view);
            ivPageImage = (ImageView) view.findViewById(R.id.ivPageImage);
        }

        private void bind(final Page pageItem, final OnItemClickListener listener) {

            float preload = 0.25f;
            if (itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                preload = 1;
            }

            Glide.with(itemView.getContext())
                    .load(MangaApiHelper.buildUrl(pageItem.getUrl()))
                    .thumbnail(preload)
                    .crossFade()
                    .error(R.drawable.noimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPageImage);

            ivPageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pageItem, getAdapterPosition());
                }
            });
        }
    }


    interface OnItemClickListener {
        void onItemClick(Page page, int position);
    }
}

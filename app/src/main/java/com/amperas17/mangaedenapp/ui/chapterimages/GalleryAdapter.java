package com.amperas17.mangaedenapp.ui.chapterimages;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.page.Page;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    ArrayList<Page> pageList;
    private OnItemClickListener listener;

    GalleryAdapter(OnItemClickListener listener) {
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
        PhotoView ivPageImage;

        private ViewHolder(View view) {
            super(view);
            ivPageImage = (PhotoView) view.findViewById(R.id.ivPageImage);
        }

        private void bind(final Page pageItem, final OnItemClickListener listener) {

            Glide.with(itemView.getContext())
                    .load(itemView.getContext().getString(R.string.image_url_prefix) + pageItem.getUrl())
                    .thumbnail(0.25f)
                    .crossFade()
                    .error(R.drawable.noimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPageImage);

            ivPageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pageItem);
                }
            });
        }
    }


    interface OnItemClickListener {
        void onItemClick(Page page);
    }
}

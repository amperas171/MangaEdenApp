package com.amperas17.mangaedenapp.ui.chapterimages;


import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.image.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ChapterImageAdapter extends RecyclerView.Adapter<ChapterImageAdapter.ViewHolder> {

    ArrayList<Image> imageList;
    private OnItemClickListener listener;

    ChapterImageAdapter(OnItemClickListener listener) {
        this.imageList = new ArrayList<>();
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
        holder.bind(imageList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPageImage;

        private ViewHolder(View view) {
            super(view);
            ivPageImage = (ImageView) view.findViewById(R.id.ivPageImage);
        }

        private void bind(final Image pageItem, final OnItemClickListener listener) {

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
        void onItemClick(Image page, int position);
    }
}

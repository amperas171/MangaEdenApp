package com.amperas17.mangaedenapp.ui.mangalist.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.amperas17.mangaedenapp.utils.adapter.AdapterItemClickListener;
import com.amperas17.mangaedenapp.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> {

    private ArrayList<Manga> mangaList;
    private AdapterItemClickListener<Manga> listener;

    MangaAdapter(AdapterItemClickListener<Manga> listener) {
        this.mangaList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manga, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bind(mangaList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    ArrayList<Manga> getMangaList() {
        return mangaList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMangaImage;
        private TextView tvMangaTitle;
        private TextView tvMangaHits;
        private TextView tvMangaDate;

        private ViewHolder(final View itemView) {
            super(itemView);
            ivMangaImage = itemView.findViewById(R.id.ivMangaImage);
            tvMangaTitle = itemView.findViewById(R.id.tvMangaTitle);
            tvMangaHits = itemView.findViewById(R.id.tvMangaHits);
            tvMangaDate = itemView.findViewById(R.id.tvMangaDate);
        }

        private void bind(final Manga mangaItem, final AdapterItemClickListener<Manga> listener) {

            Picasso.with(ivMangaImage.getContext())
                    .load(MangaApiHelper.buildUrl(mangaItem.getImage()))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(ivMangaImage);

            tvMangaTitle.setText(mangaItem.getTitle());
            tvMangaHits.setText(String.valueOf(mangaItem.getHits()));
            tvMangaDate.setText(DateUtils.defaultFormatFromSeconds(mangaItem.getLastChapterDate()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mangaItem);
                }
            });
        }

    }

}


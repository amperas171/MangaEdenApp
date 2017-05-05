package com.amperas17.mangaedenapp.ui.mangalist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.manga.Manga;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> {

    ArrayList<Manga> mangaList;
    private OnItemClickListener listener;

    MangaAdapter(OnItemClickListener listener) {
        this.mangaList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manga, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bind(mangaList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMangaImage;
        private TextView tvMangaTitle;
        private TextView tvMangaHits;
        private TextView tvMangaDate;

        private ViewHolder(final View itemView) {
            super(itemView);
            ivMangaImage = (ImageView) itemView.findViewById(R.id.ivMangaImage);
            tvMangaTitle = (TextView) itemView.findViewById(R.id.tvMangaTitle);
            tvMangaHits = (TextView) itemView.findViewById(R.id.tvMangaHits);
            tvMangaDate = (TextView) itemView.findViewById(R.id.tvMangaDate);
        }

        private void bind(final Manga mangaItem, final OnItemClickListener listener) {
            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            Picasso.with(ivMangaImage.getContext())
                    .load(itemView.getContext().getString(R.string.image_url_prefix) + (mangaItem.getImage()))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(ivMangaImage);

            tvMangaTitle.setText(mangaItem.getTitle());
            tvMangaHits.setText("" + mangaItem.getHits());
            tvMangaDate.setText(DATE_FORMAT.format(new Date(1000 * mangaItem.getLastChapterDate())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mangaItem);
                }
            });
        }

    }

    interface OnItemClickListener {
        void onItemClick(Manga manga);
    }

}


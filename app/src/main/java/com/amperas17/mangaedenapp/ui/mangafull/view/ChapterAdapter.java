package com.amperas17.mangaedenapp.ui.mangafull.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.chapter.Chapter;
import com.amperas17.mangaedenapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;


class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    ArrayList<Chapter> chapterList;
    private OnItemClickListener listener;

    ChapterAdapter(OnItemClickListener listener) {
        this.chapterList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bind(chapterList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMangaTitle;
        private TextView tvChapterTitle;
        private TextView tvDate;

        private ViewHolder(final View itemView) {
            super(itemView);
            tvMangaTitle = itemView.findViewById(R.id.tvMangaTitle);
            tvChapterTitle = itemView.findViewById(R.id.tvChapterTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        private void bind(final Chapter chapterItem, final OnItemClickListener listener) {
            tvMangaTitle.setText(String.valueOf(chapterItem.getNumber()));
            tvChapterTitle.setText(chapterItem.getTitle());
            tvDate.setText(DateUtils.DATE_FORMAT.format(new Date(DateUtils.MILLIS_IN_SECOND * chapterItem.getDate())));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chapterItem);
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(Chapter chapter);
    }
}

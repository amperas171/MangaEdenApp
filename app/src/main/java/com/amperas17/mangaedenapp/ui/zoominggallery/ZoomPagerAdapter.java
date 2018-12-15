package com.amperas17.mangaedenapp.ui.zoominggallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.page.Page;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;


public class ZoomPagerAdapter extends PagerAdapter {

    ArrayList<Page> pageList;

    public ZoomPagerAdapter(ArrayList<Page> pageList) {
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.item_pager_zoom, container, false);

        PhotoView photoView = itemView.findViewById(R.id.photo_view);

        //For very long images
        int weight = Target.SIZE_ORIGINAL;
        if (pageList.get(position).getHeight() > 2000) weight = 430;

        Glide.with(container.getContext())
                .load(MangaApiHelper.buildUrl(pageList.get(position).getUrl()))
                .error(R.drawable.noimage)
                .override(weight, Target.SIZE_ORIGINAL)
                .into(photoView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
}

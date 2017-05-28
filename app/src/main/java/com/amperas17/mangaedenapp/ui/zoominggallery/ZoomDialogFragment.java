package com.amperas17.mangaedenapp.ui.zoominggallery;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.image.Image;

import java.util.ArrayList;


public class ZoomDialogFragment extends DialogFragment {

    public static final String IMAGES_TAG = "images";
    public static final String IMAGE_POSITION = "position";

    public static ZoomDialogFragment newInstance(ArrayList<Image> pageList, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IMAGES_TAG, pageList);
        bundle.putInt(IMAGE_POSITION,position);

        ZoomDialogFragment fragment = new ZoomDialogFragment();
        fragment.setArguments(bundle);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_zoom_image, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ArrayList<Image> pageList = getArguments().getParcelableArrayList(IMAGES_TAG);
        int position = getArguments().getInt(IMAGE_POSITION);

        ViewPager pager = (ViewPager) getView().findViewById(R.id.pager);
        ZoomPagerAdapter adapter = new ZoomPagerAdapter(pageList);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);

    }

}
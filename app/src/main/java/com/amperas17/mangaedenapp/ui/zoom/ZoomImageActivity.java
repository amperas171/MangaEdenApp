package com.amperas17.mangaedenapp.ui.zoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.image.Image;

import java.util.ArrayList;


public class ZoomImageActivity extends AppCompatActivity {

    public static final String IMAGES_TAG = "images";
    public static final String IMAGE_POSITION = "position";
    public static final String POSITION_TAG = "position";

    ZoomViewPager pager;

    ArrayList<Image> pageList;

    public static Intent newIntent(Context context, ArrayList<Image> imageList, int position) {
        Intent intent = new Intent(context, ZoomImageActivity.class);
        intent.putParcelableArrayListExtra(IMAGES_TAG, imageList);
        intent.putExtra(IMAGE_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(R.layout.activity_zoom_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pageList = getIntent().getExtras().getParcelableArrayList(IMAGES_TAG);
        int position = getIntent().getExtras().getInt(IMAGE_POSITION);

        pager = (ZoomViewPager) findViewById(R.id.pager);
        ZoomPagerAdapter adapter = new ZoomPagerAdapter(pageList);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(POSITION_TAG,pager.getCurrentItem());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}

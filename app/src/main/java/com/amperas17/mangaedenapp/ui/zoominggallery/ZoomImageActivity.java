package com.amperas17.mangaedenapp.ui.zoominggallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.utils.ActivityUtils;

import java.util.ArrayList;


public class ZoomImageActivity extends AppCompatActivity {

    public static final String IMAGES_TAG = "images";
    public static final String POSITION_TAG = "position";

    ZoomViewPager pager;
    ArrayList<Page> pageList;

    public static Intent newIntent(Context context, ArrayList<Page> pageList, int position) {
        Intent intent = new Intent(context, ZoomImageActivity.class);
        intent.putParcelableArrayListExtra(IMAGES_TAG, pageList);
        intent.putExtra(POSITION_TAG, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.makeFullScreen(this);
        setContentView(R.layout.activity_zoom);
        initPager();
    }

    @Override
    public void onBackPressed() {
        returnResultOnExit();
        super.onBackPressed();
    }

    private void returnResultOnExit() {
        Intent intent = new Intent();
        intent.putExtra(POSITION_TAG, pager.getCurrentItem());
        setResult(RESULT_OK, intent);
    }

    private void initPager() {
        pageList = getIntent().getExtras().getParcelableArrayList(IMAGES_TAG);
        int position = getIntent().getExtras().getInt(POSITION_TAG);

        pager = findViewById(R.id.pager);
        ZoomPagerAdapter adapter = new ZoomPagerAdapter(pageList);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
    }
}

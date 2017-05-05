package com.amperas17.mangaedenapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.page.Page;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class ZoomImageActivity extends AppCompatActivity {

    public static final String IMAGE_TAG = "image";

    private Page page;

    public static Intent newIntent(Context context, Page page) {
        Intent intent = new Intent(context, ZoomImageActivity.class);
        intent.putExtra(IMAGE_TAG, page);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        page = (Page) getIntent().getExtras().getSerializable(IMAGE_TAG);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        //photoView.setImageResource(R.drawable.noimage);

        Picasso.with(ZoomImageActivity.this)
                .load(getString(R.string.image_url_prefix) + page.getUrl())
                .placeholder(R.drawable.loading)
                .error(R.drawable.noimage)
                .into(photoView);
    }
}

package com.amperas17.mangaedenapp.ui.chapterimages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.model.image.Image;
import com.amperas17.mangaedenapp.model.image.ImageListResponse;
import com.amperas17.mangaedenapp.ui.zoom.ZoomImageActivity;
import com.amperas17.mangaedenapp.ui.zoom.ZoomDialogFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterImagesActivity extends AppCompatActivity {

    public static final String CHAPTER_ID_TAG = "ChapterID";
    public static final String CHAPTER_TITLE_TAG = "ChapterTitle";
    public static final int ZOOM_ACTIVITY_REQUEST_CODE = 101;
    public static final String POSITION_TAG = "position";

    private ChapterImageAdapter galleryAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Image> pageList = new ArrayList<>();
    private String chapterID;
    private String chapterTitle;

    public static Intent newIntent(Context context, String chapterID, String chapterTitle) {
        Intent intent = new Intent(context, ChapterImagesActivity.class);
        intent.putExtra(CHAPTER_ID_TAG, chapterID);
        intent.putExtra(CHAPTER_TITLE_TAG, chapterTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_images);

        chapterID = getIntent().getExtras().getString(CHAPTER_ID_TAG);

        chapterTitle = getIntent().getExtras().getString(CHAPTER_TITLE_TAG);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chapterTitle);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        galleryAdapter = new ChapterImageAdapter(new ChapterImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image page, int position) {
                startActivityForResult(
                        ZoomImageActivity.newIntent(ChapterImagesActivity.this,pageList, position),
                        ZOOM_ACTIVITY_REQUEST_CODE
                );
                //showDialog(page);
                //showZoomDialog(imageList, position);
                //Toast.makeText(ChapterImagesActivity.this,page.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);

        callDataRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ZOOM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(POSITION_TAG,0);
                recyclerView.scrollToPosition(position);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void callDataRequest() {
        MangaEdenApp.getMangaApi().getPageList(chapterID).enqueue(new Callback<ImageListResponse>() {
            @Override
            public void onResponse(Call<ImageListResponse> call, Response<ImageListResponse> response) {
                pageList.addAll(response.body().getImages());
                addDataToAdapter(response.body().getImages());
            }

            @Override
            public void onFailure(Call<ImageListResponse> call, Throwable t) {

            }
        });
    }

    private void addDataToAdapter(ArrayList<Image> list) {
        galleryAdapter.imageList.clear();
        galleryAdapter.imageList.addAll(list);
        galleryAdapter.notifyDataSetChanged();
    }


    void showDialog(Image page) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = MyDialogFragment.newInstance(page);
        newFragment.show(ft, "dialog");
    }

    void showZoomDialog(ArrayList<Image> pageList, int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ZoomDialogFragment newFragment = ZoomDialogFragment.newInstance(pageList, position);
        newFragment.show(ft, "zoomDialog");
    }

    public static class MyDialogFragment extends DialogFragment {

        public static final String IMAGE_TAG = "image";

        static MyDialogFragment newInstance(Image page) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(IMAGE_TAG, page);

            MyDialogFragment fragment = new MyDialogFragment();
            fragment.setArguments(bundle);
            fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_zoom_image, container, false);
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Image page = getArguments().getParcelable(IMAGE_TAG);

            PhotoView photoView = (PhotoView) getView().findViewById(R.id.photo_view);

            Picasso.with(getView().getContext())
                    .load(MangaApiHelper.buildUrl(page.getUrl()))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(photoView);
        }

    }



    /*public static class MyDialogFragment extends DialogFragment {
        public static final String IMAGE_TAG = "image";

        static MyDialogFragment newInstance(Image page) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IMAGE_TAG, page);

            MyDialogFragment fragment = new MyDialogFragment();
            fragment.setArguments(bundle);
            fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_zoom_image, container, false);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Image page = (Image) getArguments().getSerializable(IMAGE_TAG);

            PhotoView photoView = (PhotoView) getView().findViewById(R.id.photo_view);

            Picasso.with(getView().getContext())
                    .load(getString(R.string.image_url_prefix) + page.getUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(photoView);
        }
    }*/
}

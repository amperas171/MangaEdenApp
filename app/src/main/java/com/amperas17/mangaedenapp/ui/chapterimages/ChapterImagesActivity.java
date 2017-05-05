package com.amperas17.mangaedenapp.ui.chapterimages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageListResponse;
import com.amperas17.mangaedenapp.ui.ZoomImageActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterImagesActivity extends AppCompatActivity {

    public static final String CHAPTER_ID_TAG = "ChapterID";
    public static final String CHAPTER_TITLE_TAG = "ChapterTitle";

    private GalleryAdapter galleryAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Page> pageList = new ArrayList<>();
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chapterTitle);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        galleryAdapter = new GalleryAdapter(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Page page) {
                //startActivity(ZoomImageActivity.newIntent(ChapterImagesActivity.this,page));
                showDialog(page);
                //Toast.makeText(ChapterImagesActivity.this,page.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);


        callDataRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void callDataRequest(){
        MangaEdenApp.getMangaApi().getPageList(chapterID).enqueue(new Callback<PageListResponse>() {
            @Override
            public void onResponse(Call<PageListResponse> call, Response<PageListResponse> response) {
                pageList.addAll(response.body().getPages());
                addDataToAdapter(response.body().getPages());
            }

            @Override
            public void onFailure(Call<PageListResponse> call, Throwable t) {

            }
        });
    }

    private void addDataToAdapter(ArrayList<Page> list){
        galleryAdapter.pageList.clear();
        galleryAdapter.pageList.addAll(list);
        galleryAdapter.notifyDataSetChanged();
    }


    void showDialog(Page page) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = MyDialogFragment.newInstance(page);
        newFragment.show(ft, "dialog");
    }

    public static class MyDialogFragment extends DialogFragment {

        public static final String IMAGE_TAG = "image";

        static MyDialogFragment newInstance(Page page) {
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
            View v = inflater.inflate(R.layout.activity_zoom_image, container, false);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Page page = (Page) getArguments().getSerializable(IMAGE_TAG);

            PhotoView photoView = (PhotoView) getView().findViewById(R.id.photo_view);

            Picasso.with(getView().getContext())
                    .load(getString(R.string.image_url_prefix) + page.getUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(photoView);
        }
    }
}

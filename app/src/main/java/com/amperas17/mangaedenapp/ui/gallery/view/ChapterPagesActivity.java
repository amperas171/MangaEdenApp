package com.amperas17.mangaedenapp.ui.gallery.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.data.ChapterRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.ui.gallery.viewmodel.ChapterPagesViewModel;
import com.amperas17.mangaedenapp.ui.zoominggallery.ZoomImageActivity;

import java.util.ArrayList;

public class ChapterPagesActivity extends AppCompatActivity implements ChapterRepository.IGetChapter {

    public static final String CHAPTER_ID_TAG = "ChapterID";
    public static final String CHAPTER_TITLE_TAG = "ChapterTitle";

    public static final int ZOOM_ACTIVITY_REQUEST_CODE = 101;
    public static final String POSITION_TAG = "position";

    private ChapterPagesViewModel viewModel;

    private PageAdapter galleryAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Page> pageList = new ArrayList<>();

    public static Intent newIntent(Context context, String chapterID, String chapterTitle) {
        Intent intent = new Intent(context, ChapterPagesActivity.class);
        intent.putExtra(CHAPTER_ID_TAG, chapterID);
        intent.putExtra(CHAPTER_TITLE_TAG, chapterTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_pages);

        String chapterID = getIntent().getExtras().getString(CHAPTER_ID_TAG);
        String chapterTitle = getIntent().getExtras().getString(CHAPTER_TITLE_TAG);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initActionBar();
        initAdapter();
        initViewModel();

        callDataRequest(chapterID);
    }

    private void initActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void initAdapter() {
        recyclerView = findViewById(R.id.recycler_view);
        galleryAdapter = new PageAdapter(new PageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Page page, int position) {
                startActivityForResult(
                        ZoomImageActivity.newIntent(ChapterPagesActivity.this, pageList, position),
                        ZOOM_ACTIVITY_REQUEST_CODE
                );
            }
        });


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(galleryAdapter);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChapterPagesViewModel.class);
        viewModel.getResource().observe(this, new Observer<Resource<ArrayList<Page>>>() {
            @Override
            public void onChanged(@Nullable Resource<ArrayList<Page>> resource) {
                onResourceChanged(resource);
            }
        });
    }

    private void onResourceChanged(Resource<ArrayList<Page>> resource) {
        if (resource != null) {
            if (resource.getData() != null)
                onGetData(resource.getData());
            if (resource.getThrowable() != null)
                onError(resource.getThrowable());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ZOOM_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(POSITION_TAG, 0);
                recyclerView.scrollToPosition(position);
            }
        }
    }

    private void callDataRequest(String chapterID) {
        viewModel.startLoading(chapterID);
    }

    private void addDataToAdapter(ArrayList<Page> list) {
        galleryAdapter.pageList.clear();
        galleryAdapter.pageList.addAll(list);
        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetData(ArrayList<Page> pages) {
        pageList.addAll(pages);
        addDataToAdapter(pages);
    }

    @Override
    public void onError(Throwable t) {
    }
}

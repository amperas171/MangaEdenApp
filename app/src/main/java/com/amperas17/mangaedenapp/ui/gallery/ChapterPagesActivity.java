package com.amperas17.mangaedenapp.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.data.ChapterProvider;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.ui.zoominggallery.ZoomImageActivity;

import java.util.ArrayList;

public class ChapterPagesActivity extends AppCompatActivity implements ChapterProvider.IGetChapter {

    public static final String CHAPTER_ID_TAG = "ChapterID";
    public static final String CHAPTER_TITLE_TAG = "ChapterTitle";

    public static final int ZOOM_ACTIVITY_REQUEST_CODE = 101;
    public static final String POSITION_TAG = "position";

    private ChapterProvider chapterProvider;

    private PageAdapter galleryAdapter;
    private RecyclerView recyclerView;

    private ArrayList<Page> pageList = new ArrayList<>();
    private String chapterID;

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

        chapterProvider = new ChapterProvider(this);

        chapterID = getIntent().getExtras().getString(CHAPTER_ID_TAG);
        String chapterTitle = getIntent().getExtras().getString(CHAPTER_TITLE_TAG);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

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

        callDataRequest();
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

    private void callDataRequest() {
        chapterProvider.callData(chapterID);
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

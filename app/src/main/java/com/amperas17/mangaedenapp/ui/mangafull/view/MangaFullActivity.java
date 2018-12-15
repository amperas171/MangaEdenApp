package com.amperas17.mangaedenapp.ui.mangafull.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.api.MangaApiHelper;
import com.amperas17.mangaedenapp.data.MangaFullRepository;
import com.amperas17.mangaedenapp.model.Resource;
import com.amperas17.mangaedenapp.model.chapter.Chapter;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.ui.gallery.ChapterPagesActivity;
import com.amperas17.mangaedenapp.ui.mangafull.viewmodel.MangaFullViewModel;
import com.amperas17.mangaedenapp.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class MangaFullActivity extends AppCompatActivity implements MangaFullRepository.IGetMangaFull {

    public static final String MANGA_ID_TAG = "MangaID";
    public static final String MANGA_TITLE_TAG = "MangaTitle";

    private MangaFullViewModel viewModel;

    private ChapterAdapter chapterAdapter;

    private NestedScrollView mangaFullContainer;
    private TextView tvReleased, tvHits, tvAuthor, tvCategories, tvDescription;
    private ImageView ivFullMangaImage;
    private ProgressBar progressBar;


    public static Intent newIntent(Context context, String mangaID, String mangaTitle) {
        Intent intent = new Intent(context, MangaFullActivity.class);
        intent.putExtra(MANGA_ID_TAG, mangaID);
        intent.putExtra(MANGA_TITLE_TAG, mangaTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_full);

        mangaFullContainer = findViewById(R.id.mangaFullContainer);
        tvReleased = findViewById(R.id.tvReleased);
        tvHits = findViewById(R.id.tvHits);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategories = findViewById(R.id.tvCategories);
        tvDescription = findViewById(R.id.tvDescription);
        ivFullMangaImage = findViewById(R.id.ivFullMangaImage);
        progressBar = findViewById(R.id.progressBar);

        String mangaID = getIntent().getExtras().getString(MANGA_ID_TAG);
        String mangaTitle = getIntent().getExtras().getString(MANGA_TITLE_TAG);

        initActionBar(mangaTitle);
        initAdapter();
        initViewModel();

        callDataRequest(mangaID);
    }

    private void initActionBar(String mangaTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mangaTitle);
        }
    }

    private void initAdapter() {
        chapterAdapter = new ChapterAdapter(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter chapter) {
                if (chapter != null) {
                    startActivity(ChapterPagesActivity.newIntent(MangaFullActivity.this, chapter.getID(), chapter.getTitle()));
                } else {
                    Toast.makeText(MangaFullActivity.this, R.string.incorrect_chapter_id, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewChapterList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chapterAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MangaFullViewModel.class);
        viewModel.getResource().observe(this, new Observer<Resource<MangaFullInfo>>() {
            @Override
            public void onChanged(@Nullable Resource<MangaFullInfo> resource) {
                onResourceChanged(resource);
            }
        });
    }

    private void onResourceChanged(Resource<MangaFullInfo> resource) {
        if (resource != null) {
            if (resource.getData() != null)
                onGetData(resource.getData());
            if (resource.getThrowable() != null)
                onError(resource.getThrowable());
        }
    }

    private void callDataRequest(String mangaID) {
        progressBar.setVisibility(View.VISIBLE);
        mangaFullContainer.setVisibility(View.GONE);
        viewModel.startLoading(mangaID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetData(MangaFullInfo mangaFullInfo) {
        initView(mangaFullInfo);
    }

    @Override
    public void onError(Throwable t) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(MangaFullActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == View.VISIBLE)
            viewModel.stopLoading();
        super.onBackPressed();
    }

    private void initView(MangaFullInfo mangaFullInfo) {

        Picasso.with(MangaFullActivity.this)
                .load(MangaApiHelper.buildUrl(mangaFullInfo.getImage()))
                .placeholder(R.drawable.loading)
                .error(R.drawable.noimage)
                .into(ivFullMangaImage);

        tvAuthor.setText(mangaFullInfo.getAuthor());
        tvHits.setText(String.valueOf(mangaFullInfo.getHits()));
        tvReleased.setText(DateUtils.DATE_FORMAT.format(new Date(DateUtils.MILLIS_IN_SECOND * mangaFullInfo.getLast_chapter_date())));
        tvDescription.setText(Html.fromHtml(mangaFullInfo.getDescription()));
        tvCategories.setText(mangaFullInfo.getCategoriesAsString());

        addDataToAdapter(mangaFullInfo.getChapters());

        progressBar.setVisibility(View.GONE);
        mangaFullContainer.setVisibility(View.VISIBLE);
    }

    private void addDataToAdapter(ArrayList<Chapter> chapters) {
        chapterAdapter.chapterList.clear();
        chapterAdapter.chapterList.addAll(chapters);
        chapterAdapter.notifyDataSetChanged();
    }
}
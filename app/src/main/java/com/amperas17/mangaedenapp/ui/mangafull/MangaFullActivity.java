package com.amperas17.mangaedenapp.ui.mangafull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amperas17.mangaedenapp.MangaEdenApp;
import com.amperas17.mangaedenapp.R;
import com.amperas17.mangaedenapp.model.chapter.Chapter;
import com.amperas17.mangaedenapp.model.manga.MangaFullInfo;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.ui.chapterimages.ChapterImagesActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaFullActivity extends AppCompatActivity {

    public static final String MANGA_ID_TAG = "MangaID";
    public static final String MANGA_TITLE_TAG = "MangaTitle";

    private ChapterAdapter chapterAdapter;
    private RecyclerView recyclerView;

    private NestedScrollView mangaFullContainer;
    private TextView tvReleased,tvHits,tvAuthor,tvCategories,tvDescription;
    private ImageView ivFullMangaImage;
    private ProgressBar progressBar;

    private String mangaID;
    private String mangaTitle;

    private MangaFullInfo mangaFullInfo;

    public static Intent newIntent(Context context, String mangaID, String mangaTitle){
        Intent intent = new Intent(context,MangaFullActivity.class);
        intent.putExtra(MANGA_ID_TAG,mangaID);
        intent.putExtra(MANGA_TITLE_TAG,mangaTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_full);

        mangaFullContainer = (NestedScrollView)findViewById(R.id.mangaFullContainer);
        tvReleased = (TextView) findViewById(R.id.tvReleased);
        tvHits = (TextView) findViewById(R.id.tvHits);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvCategories = (TextView) findViewById(R.id.tvCategories);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivFullMangaImage = (ImageView) findViewById(R.id.ivFullMangaImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mangaID = getIntent().getExtras().getString(MANGA_ID_TAG);
        mangaTitle = getIntent().getExtras().getString(MANGA_TITLE_TAG);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mangaTitle);


        chapterAdapter = new ChapterAdapter(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter chapter) {
                if (chapter!=null) {
                    startActivity(ChapterImagesActivity.newIntent(MangaFullActivity.this, chapter.getID(), chapter.getTitle()));
                } else {
                    Toast.makeText(MangaFullActivity.this, R.string.incorrect_chapter_id,Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChapterList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chapterAdapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView,false);

        callDataRequest();
    }

    private void callDataRequest(){
        progressBar.setVisibility(View.VISIBLE);
        mangaFullContainer.setVisibility(View.GONE);
        MangaEdenApp.getMangaApi().getMangaFullInfo(mangaID).enqueue(new Callback<MangaFullInfo>() {
            @Override
            public void onResponse(Call<MangaFullInfo> call, Response<MangaFullInfo> response) {
                final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

                mangaFullInfo = response.body();

                Picasso.with(MangaFullActivity.this)
                        .load(getString(R.string.image_url_prefix) + mangaFullInfo.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivFullMangaImage);

                tvAuthor.setText(mangaFullInfo.getAuthor());
                tvHits.setText("" + mangaFullInfo.getHits());
                tvReleased.setText(DATE_FORMAT.format(new Date(1000 * mangaFullInfo.getLast_chapter_date())));
                tvDescription.setText(Html.fromHtml(mangaFullInfo.getDescription()));
                tvCategories.setText(mangaFullInfo.getCategoriesAsString());

                addDataToAdapter(mangaFullInfo.getChapters());

                progressBar.setVisibility(View.GONE);
                mangaFullContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MangaFullInfo> call, Throwable t) {
                Log.d("mmanga", "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MangaFullActivity.this, t.getMessage(),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDataToAdapter(ArrayList<Chapter> chapters) {
        chapterAdapter.chapterList.clear();
        chapterAdapter.chapterList.addAll(chapters);
        chapterAdapter.notifyDataSetChanged();
    }

}

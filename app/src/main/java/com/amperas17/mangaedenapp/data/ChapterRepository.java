package com.amperas17.mangaedenapp.data;


import android.content.Context;

import com.amperas17.mangaedenapp.api.responseprovider.BaseResponseProvider;
import com.amperas17.mangaedenapp.api.responseprovider.ChapterResponseProvider;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageListResponse;

import java.util.ArrayList;

public class ChapterRepository implements BaseResponseProvider.IGetData<PageListResponse> {

    private Context context;
    private ChapterResponseProvider responseProvider;

    public ChapterRepository(Context context) {
        this.context = context;
        responseProvider = new ChapterResponseProvider(this);
    }

    public void callData(String... args) {
        responseProvider.makeCall(args);
    }

    public void cancelDataRequest() {
        responseProvider.stopRequest();
    }

    @Override
    public void onGetData(PageListResponse pageListResponse) {
        ArrayList<Page> list = pageListResponse.getPages();
        ((IGetChapter) context).onGetData(list);
    }

    @Override
    public void onError(Throwable t) {
        ((IGetChapter) context).onError(t);
    }


    public interface IGetChapter {
        void onGetData(ArrayList<Page> pages);

        void onError(Throwable t);
    }
}

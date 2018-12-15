package com.amperas17.mangaedenapp.data;

import com.amperas17.mangaedenapp.api.responseprovider.ChapterResponseProvider;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageListResponse;
import com.amperas17.mangaedenapp.utils.Caller;

import java.util.ArrayList;


public class ChapterRepository implements Caller<PageListResponse> {

    private Caller<ArrayList<Page>> caller;
    private ChapterResponseProvider responseProvider;

    public ChapterRepository(Caller<ArrayList<Page>> caller) {
        this.caller = caller;
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
        if (pageListResponse != null) {
            ArrayList<Page> list = pageListResponse.getPages();
            caller.onGetData(list);
        }
    }

    @Override
    public void onError(Throwable t) {
        caller.onError(t);
    }
}

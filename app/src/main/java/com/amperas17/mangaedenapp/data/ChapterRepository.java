package com.amperas17.mangaedenapp.data;

import com.amperas17.mangaedenapp.api.responseprovider.BaseResponseProvider;
import com.amperas17.mangaedenapp.api.responseprovider.ChapterResponseProvider;
import com.amperas17.mangaedenapp.model.page.Page;
import com.amperas17.mangaedenapp.model.page.PageListResponse;

import java.util.ArrayList;

public class ChapterRepository implements BaseResponseProvider.IGetData<PageListResponse> {

    private IGetChapter caller;
    private ChapterResponseProvider responseProvider;

    public ChapterRepository(IGetChapter caller) {
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


    public interface IGetChapter {
        void onGetData(ArrayList<Page> pages);

        void onError(Throwable t);
    }
}

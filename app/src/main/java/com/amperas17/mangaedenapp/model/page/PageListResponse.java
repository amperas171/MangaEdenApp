package com.amperas17.mangaedenapp.model.page;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PageListResponse {

    @SerializedName("images")
    private List<Page> pages;

    public PageListResponse(List<Page> pages) {
        this.pages = pages;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "PageListResponse{" +
                "\npages=" + pages +
                '}';
    }
}

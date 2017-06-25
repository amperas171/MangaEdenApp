package com.amperas17.mangaedenapp.model.page;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class PageListResponse {

    @SerializedName("images")
    private ArrayList<Page> pages;

    public PageListResponse(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "PageListResponse{" +
                "\npages=" + pages +
                '}';
    }
}

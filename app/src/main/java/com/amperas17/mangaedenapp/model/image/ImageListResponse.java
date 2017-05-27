package com.amperas17.mangaedenapp.model.image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ImageListResponse {

    @SerializedName("images")
    private ArrayList<Image> images;

    public ImageListResponse(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImageListResponse{" +
                "\nimages=" + images +
                '}';
    }
}

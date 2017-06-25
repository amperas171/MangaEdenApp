package com.amperas17.mangaedenapp.api;


public class MangaApiHelper {
    public static String IMAGE_URL_PREFIX = "http://cdn.mangaeden.com/mangasimg/";
    public static int ENGLISH = 0;

    public static String buildUrl(String imageUrl) {
        return IMAGE_URL_PREFIX + imageUrl;
    }
}

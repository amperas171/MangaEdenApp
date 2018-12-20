package com.amperas17.mangaedenapp.api


object MangaApiHelper {
    private var IMAGE_URL_PREFIX = "https://cdn.mangaeden.com/mangasimg/"
    var ENGLISH = 0

    fun buildUrl(imageUrl: String): String {
        return "$IMAGE_URL_PREFIX$imageUrl"
    }
}

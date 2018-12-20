package com.amperas17.mangaedenapp.model.manga

import com.google.gson.annotations.SerializedName

import java.util.ArrayList


data class MangaListResponse(
        var end: Int,
        @SerializedName("manga")
        var mangas: ArrayList<Manga>?,
        var page: Int,
        var start: Int,
        var total: Int
)

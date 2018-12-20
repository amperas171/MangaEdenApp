package com.amperas17.mangaedenapp.model.page

import com.google.gson.annotations.SerializedName

import java.util.ArrayList


data class PageListResponse(
        @SerializedName("images")
        var pages: ArrayList<Page>?
)

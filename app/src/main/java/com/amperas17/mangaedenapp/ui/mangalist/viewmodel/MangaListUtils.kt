package com.amperas17.mangaedenapp.ui.mangalist.viewmodel

import com.amperas17.mangaedenapp.model.manga.Manga

import java.util.ArrayList

fun ArrayList<Manga>.findForMatchesInTitle(pattern: String): List<Manga> {
    return filter { manga ->
        manga.title?.toLowerCase()?.startsWith(pattern.toLowerCase()) == true ||
                manga.title?.toLowerCase()?.contains(" " + pattern.toLowerCase()) == true
    }.sortedBy { it.hits }
}

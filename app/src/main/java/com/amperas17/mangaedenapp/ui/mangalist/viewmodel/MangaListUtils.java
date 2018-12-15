package com.amperas17.mangaedenapp.ui.mangalist.viewmodel;

import com.amperas17.mangaedenapp.model.manga.Manga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class MangaListUtils {
    static ArrayList<Manga> findForMatchesInTitle(String pattern, ArrayList<Manga> mangaListAll) {
        ArrayList<Manga> resultList = new ArrayList<>();
        for (Manga manga : mangaListAll) {
            if (manga.getTitle().toLowerCase().startsWith(pattern.toLowerCase())
                    || manga.getTitle().toLowerCase().contains(" " + pattern.toLowerCase())) {
                resultList.add(manga);
            }
        }
        Collections.sort(resultList, new Comparator<Manga>() {
            @Override
            public int compare(Manga manga1, Manga manga2) {
                return manga2.getHits() - manga1.getHits();
            }
        });
        return resultList;
    }
}

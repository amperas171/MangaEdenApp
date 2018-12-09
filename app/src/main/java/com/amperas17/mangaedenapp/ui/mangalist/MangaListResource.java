package com.amperas17.mangaedenapp.ui.mangalist;

import com.amperas17.mangaedenapp.model.manga.Manga;

import java.util.ArrayList;

public class MangaListResource {

    private ArrayList<Manga> mangas;
    private Throwable throwable;

    public MangaListResource(ArrayList<Manga> mangas) {
        this.mangas = mangas;
        this.throwable = null;
    }

    public MangaListResource(Throwable error) {
        this.throwable = error;
        this.mangas = null;
    }

    public ArrayList<Manga> getMangas() {
        return mangas;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}

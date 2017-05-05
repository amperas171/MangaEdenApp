package com.amperas17.mangaedenapp.model.manga;


import com.amperas17.mangaedenapp.model.chapter.Chapter;

import java.util.ArrayList;

public class MangaFullInfo {
    private String artist;
    private ArrayList<String> artist_kw;
    private String author;
    private ArrayList<String> author_kw;

    private ArrayList<String> categories;
    private ArrayList<Chapter> chapters;
    private int chapters_len;
    private double created;
    private String description;
    private int hits;
    private String image;
    private String imageURL;

    private int language;

    private long last_chapter_date;
    private long released;
    private String startsWith;
    private String title;
    private ArrayList<String> title_kw;

    public MangaFullInfo(String artist, ArrayList<String> artist_kw, String author, ArrayList<String> author_kw, ArrayList<String> categories, ArrayList<Chapter> chapters, int chapters_len, double created, String description, int hits, String image, String imageURL, int language, long last_chapter_date, long released, String startsWith, String title, ArrayList<String> title_kw) {
        this.artist = artist;
        this.artist_kw = artist_kw;
        this.author = author;
        this.author_kw = author_kw;
        this.categories = categories;
        this.chapters = chapters;
        this.chapters_len = chapters_len;
        this.created = created;
        this.description = description;
        this.hits = hits;
        this.image = image;
        this.imageURL = imageURL;
        this.language = language;
        this.last_chapter_date = last_chapter_date;
        this.released = released;
        this.startsWith = startsWith;
        this.title = title;
        this.title_kw = title_kw;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public ArrayList<String> getArtist_kw() {
        return artist_kw;
    }

    public void setArtist_kw(ArrayList<String> artist_kw) {
        this.artist_kw = artist_kw;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getAuthor_kw() {
        return author_kw;
    }

    public void setAuthor_kw(ArrayList<String> author_kw) {
        this.author_kw = author_kw;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public int getChapters_len() {
        return chapters_len;
    }

    public void setChapters_len(int chapters_len) {
        this.chapters_len = chapters_len;
    }

    public double getCreated() {
        return created;
    }

    public void setCreated(double created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public long getLast_chapter_date() {
        return last_chapter_date;
    }

    public void setLast_chapter_date(long last_chapter_date) {
        this.last_chapter_date = last_chapter_date;
    }

    public long getReleased() {
        return released;
    }

    public void setReleased(long released) {
        this.released = released;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(String startsWith) {
        this.startsWith = startsWith;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getTitle_kw() {
        return title_kw;
    }

    public void setTitle_kw(ArrayList<String> title_kw) {
        this.title_kw = title_kw;
    }

    @Override
    public String toString() {
        return "MangaFullInfo{" +
                "artist='" + artist + '\'' +
                ",\n artist_kw=" + artist_kw +
                ",\n author='" + author + '\'' +
                ",\n author_kw=" + author_kw +
                ",\n categories=" + categories +
                ",\n chapters=" + chapters +
                ",\n chapters_len=" + chapters_len +
                ",\n created=" + created +
                ",\n description='" + description + '\'' +
                ",\n hits=" + hits +
                ",\n image='" + image + '\'' +
                ",\n imageURL='" + imageURL + '\'' +
                ",\n language=" + language +
                ",\n last_chapter_date=" + last_chapter_date +
                ",\n released=" + released +
                ",\n startsWith='" + startsWith + '\'' +
                ",\n title='" + title + '\'' +
                ",\n title_kw=" + title_kw +
                '}';
    }

    public String getCategoriesAsString(){
        String result = "";
        result = categories.get(0);
        for (int i=1;i<categories.size();i++){
            result += ", " + categories.get(i);
        }
        return result;
    }
}

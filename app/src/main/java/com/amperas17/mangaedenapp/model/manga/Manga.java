package com.amperas17.mangaedenapp.model.manga;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Manga implements Parcelable, Comparable<Manga> {

    @SerializedName("a")
    private String alias;
    @SerializedName("c")
    private ArrayList<String> categories;
    @SerializedName("h")
    private int hits;
    @SerializedName("i")
    private String ID;
    @SerializedName("im")
    private String image;
    @SerializedName("ld")
    private long lastChapterDate;
    @SerializedName("s")
    private int status;
    @SerializedName("t")
    private String title;

    public Manga(String alias, ArrayList<String> categories, int hits, String ID, String image, long lastChapterDate, int status, String title) {
        this.alias = alias;
        this.categories = categories;
        this.hits = hits;
        this.ID = ID;
        this.image = image;
        this.lastChapterDate = lastChapterDate;
        this.status = status;
        this.title = title;
    }

    protected Manga(Parcel in) {
        alias = in.readString();
        categories = in.createStringArrayList();
        hits = in.readInt();
        ID = in.readString();
        image = in.readString();
        lastChapterDate = in.readLong();
        status = in.readInt();
        title = in.readString();
    }

    public static final Creator<Manga> CREATOR = new Creator<Manga>() {
        @Override
        public Manga createFromParcel(Parcel in) {
            return new Manga(in);
        }

        @Override
        public Manga[] newArray(int size) {
            return new Manga[size];
        }
    };

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getLastChapterDate() {
        return lastChapterDate;
    }

    public void setLastChapterDate(long lastChapterDate) {
        this.lastChapterDate = lastChapterDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "\nManga{" +
                "alias='" + alias + '\'' +
                ",\n categories=" + categories +
                ",\n hits=" + hits +
                ",\n ID='" + ID + '\'' +
                ",\n image='" + image + '\'' +
                ",\n lastChapterDate=" + lastChapterDate +
                ",\n status=" + status +
                ",\n title='" + title + '\'' +
                '}';
    }

    @Override
    public int compareTo(Manga otherManga) {
        if (lastChapterDate>otherManga.lastChapterDate){
            return -1;
        } else if (otherManga.lastChapterDate>lastChapterDate){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeStringList(categories);
        dest.writeInt(hits);
        dest.writeString(ID);
        dest.writeString(image);
        dest.writeLong(lastChapterDate);
        dest.writeInt(status);
        dest.writeString(title);
    }
}

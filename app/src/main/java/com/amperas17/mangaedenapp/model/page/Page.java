package com.amperas17.mangaedenapp.model.page;


import android.os.Parcel;
import android.os.Parcelable;


public class Page implements Parcelable {
    private int number;
    private String url;
    private int weight;
    private int height;

    public Page(int number, String url, int weight, int height) {
        this.number = number;
        this.url = url;
        this.weight = weight;
        this.height = height;
    }

    protected Page(Parcel in) {
        number = in.readInt();
        url = in.readString();
        weight = in.readInt();
        height = in.readInt();
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", url='" + url + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(url);
        dest.writeInt(weight);
        dest.writeInt(height);
    }
}

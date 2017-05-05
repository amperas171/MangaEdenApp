package com.amperas17.mangaedenapp.model.page;


import java.io.Serializable;

public class Page implements Serializable {
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
}

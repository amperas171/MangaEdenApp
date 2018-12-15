package com.amperas17.mangaedenapp.model;


public class Resource<T> {

    private T data;
    private Throwable throwable;

    public Resource(T data) {
        this.data = data;
        this.throwable = null;
    }

    public Resource(Throwable error) {
        this.throwable = error;
        this.data = null;
    }

    public T getData() {
        return data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}

package com.amperas17.mangaedenapp.utils;

import com.amperas17.mangaedenapp.model.page.Page;

import java.util.ArrayList;

public interface Caller<T> {
    void onGetData(T data);
    void onError(Throwable t);
}

package com.amperas17.mangaedenapp.utils

interface Caller<T> {
    fun onGetData(data: T)
    fun onError(t: Throwable)
}

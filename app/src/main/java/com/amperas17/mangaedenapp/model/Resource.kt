package com.amperas17.mangaedenapp.model


class Resource<T> {

    var data: T? = null
        private set
    var throwable: Throwable? = null
        private set

    constructor(data: T) {
        this.data = data
        this.throwable = null
    }

    constructor(error: Throwable) {
        this.throwable = error
        this.data = null
    }
}

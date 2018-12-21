package com.amperas17.mangaedenapp.api.responseprovider

import com.amperas17.mangaedenapp.api.MangaApi
import com.amperas17.mangaedenapp.utils.Caller
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseResponseProvider<T, U>(private val caller: Caller<T>): KoinComponent {
    protected val mangaApi by inject<MangaApi>()
    private var call: Call<T>? = null

    abstract fun initCall(vararg args: U): Call<T>

    fun makeCall(vararg args: U) {
        call = initCall(*args)
        call?.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                caller.onError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.body()?.let { caller.onGetData(it) }
            }
        })
    }

    fun stopRequest() {
        if (call?.isExecuted == true) {
            call?.cancel()
        }
    }
}

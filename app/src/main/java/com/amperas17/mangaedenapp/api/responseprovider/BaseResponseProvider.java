package com.amperas17.mangaedenapp.api.responseprovider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseResponseProvider<T> {

    private IGetData<T> caller;
    private Call<T> call;

    public BaseResponseProvider(IGetData<T> caller) {
        this.caller = caller;
    }

    public abstract Call<T> initCall();

    public void makeCall() {
        call = initCall();
        call.enqueue(new Callback<T>() {
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                caller.onError(t);
            }

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                caller.onGetDate(response.body());
            }
        });
    }

    public void stopRequest() {
        if (call.isExecuted())
            call.cancel();
    }

    public interface IGetData<T> {
        void onGetDate(T response);

        void onError(Throwable t);
    }
}

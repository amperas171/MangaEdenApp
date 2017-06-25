package com.amperas17.mangaedenapp.api.responseprovider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseResponseProvider<T, U> {

    private IGetData<T> caller;
    private Call<T> call;

    public BaseResponseProvider(IGetData<T> caller) {
        this.caller = caller;
    }

    public abstract Call<T> initCall(U... args);

    public void makeCall(U... args) {
        call = initCall(args);
        call.enqueue(new Callback<T>() {
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                caller.onError(t);
            }

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                caller.onGetData(response.body());
            }
        });
    }

    public void stopRequest() {
        if (call != null && call.isExecuted())
            call.cancel();
    }

    public interface IGetData<T> {
        void onGetData(T response);

        void onError(Throwable t);
    }
}

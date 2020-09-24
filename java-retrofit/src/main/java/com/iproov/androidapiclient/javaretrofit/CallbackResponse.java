package com.iproov.androidapiclient.javaretrofit;

import retrofit2.Call;
import retrofit2.Response;

public interface CallbackResponse<T> {

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     */
    void onResponse(Call<T> call, Response<T> response);
}

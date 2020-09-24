package com.iproov.androidapiclient.javaretrofit;

public interface CallbackFailure {

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    void onFailure(Throwable t);
}

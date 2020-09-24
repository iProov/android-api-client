package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

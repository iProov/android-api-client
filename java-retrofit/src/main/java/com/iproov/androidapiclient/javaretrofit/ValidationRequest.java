package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

public class ValidationRequest {
    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("secret")
    private String secret;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("token")
    private String token;
    @SerializedName("ip")
    private String ip;
    @SerializedName("client")
    private String client;

    public ValidationRequest(String apiKey, String secret, String userId, String token, String ip, String client) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.userId = userId;
        this.token = token;
        this.ip = ip;
        this.client = client;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}

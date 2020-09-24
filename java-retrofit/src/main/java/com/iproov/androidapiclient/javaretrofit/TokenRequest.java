package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

public class TokenRequest {

    @SerializedName("api_key")
    private String apiKey;
    @SerializedName("secret")
    private String secret;
    @SerializedName("resource")
    private String resource;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("client")
    private String client = "android";

    public TokenRequest(String apiKey, String secret, String resource, String userId, String client) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.resource = resource;
        this.userId = userId;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}

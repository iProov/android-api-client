package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.iproov.androidapiclient.javaretrofit.ApiClientJavaRetrofit.*;

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
    private String client;
    @SerializedName("assurance_type")
    private String assuranceType;

    private Map<String, Object> options;

    public TokenRequest(String apiKey, String secret, String resource, String userId, String client, AssuranceType assuranceType, Map<String, Object> options) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.resource = resource;
        this.userId = userId;
        this.client = client;
        this.assuranceType = assuranceType.backendName;
        this.options = options;
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

    public String getAssuranceType() { return assuranceType; }

    public void setAssuranceType(String assuranceType) { this.assuranceType = assuranceType; }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

}

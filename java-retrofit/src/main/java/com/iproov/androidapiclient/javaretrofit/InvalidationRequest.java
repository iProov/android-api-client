package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

public class InvalidationRequest {
    @SerializedName("reason")
    private String reason;

    public InvalidationRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.annotations.SerializedName;

public class InvalidationResult {
    @SerializedName("claim_aborted")
    public Boolean claimAborted;
    @SerializedName("user_informed")
    public Boolean userInformed;
}

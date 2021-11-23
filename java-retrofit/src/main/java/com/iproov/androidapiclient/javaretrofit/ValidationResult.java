package com.iproov.androidapiclient.javaretrofit;

import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;

public class ValidationResult {
    @SerializedName("passed")
    public Boolean isPassed;
    @SerializedName("token")
    public String token;
    @SerializedName("frame")
    public Bitmap frame;
    @SerializedName("reason")
    public String failureReason;
}

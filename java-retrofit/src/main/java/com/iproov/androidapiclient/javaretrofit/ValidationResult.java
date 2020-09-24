package com.iproov.androidapiclient.javaretrofit;

import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;

public class ValidationResult {
    @SerializedName("passed")
    private Boolean isPassed;
    @SerializedName("token")
    private String token;
    @SerializedName("frame")
    private Bitmap frame;
    @SerializedName("reason")
    private String failureReason;
}

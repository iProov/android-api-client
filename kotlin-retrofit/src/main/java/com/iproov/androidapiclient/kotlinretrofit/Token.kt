package com.iproov.androidapiclient.kotlinretrofit

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("token")
    val token: String
)

data class TokenRequest(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("resource")
    val resource: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("client")
    val client: String = "android",
    @SerializedName("assurance_type")
    val assuranceType: String
)

data class ValidationRequest(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("ip")
    val ip: String,
    @SerializedName("client")
    val client: String = "android"
)

data class ValidationResult(
    @SerializedName("passed")
    val isPassed: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("frame")
    val frame: Bitmap?,
    @SerializedName("reason")
    val failureReason: String?
)
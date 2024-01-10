package com.iproov.androidapiclient.kotlinretrofit

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class GetTokenRequest(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("resource")
    val resource: String,
    @SerializedName("assurance_type")
    val assuranceType: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("risk_profile")
    val riskProfile: String = "default"
)

data class GetTokenResponse (
    @SerializedName("token")
    val token: String,
    @SerializedName("primary")
    val primary: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("pod")
    val pod: String,
    @SerializedName("risk_profile")
    val riskProfile: String = "default",
)
data class PhotoEnrolResponse (
    @SerializedName("token")
    val token: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("success")
    val success: Boolean
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
    @SerializedName("client")
    val client: String = "android",
    @SerializedName("risk_profile")
    val riskProfile: String = "default"
)

data class ValidationResult(
    @SerializedName("passed")
    val isPassed: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("frame_available")
    val isFrameAvailable: Boolean,
    @SerializedName("frame")
    val frame: Bitmap?,
    @SerializedName("reason")
    val failureReason: String?,
    @SerializedName("risk_profile")
    val riskProfile: String?,
    @SerializedName("assurance_type")
    val assuranceType: String?,
    @SerializedName("signals")
    val signals: JSONObject?
)

data class InvalidationRequest(
    @SerializedName("reason")
    val reason: String
)

data class InvalidationResult(
    @SerializedName("claim_aborted")
    val claimAborted: Boolean,
    @SerializedName("user_informed")
    val userInformed: Boolean
)
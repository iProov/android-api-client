package com.iproov.androidapiclient.kotlinfuel

import android.graphics.Bitmap
import org.json.JSONObject

data class ValidationResult (
    val isPassed: Boolean,
    val token: String,
    val type: String,
    val isFrameAvailable: Boolean,
    val frame: Bitmap?,
    val failureReason: String?,
    val riskProfile: String?,
    val assuranceType: String?,
    val signals: JSONObject?
)
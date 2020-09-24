package com.iproov.androidapiclient.kotlinfuel

import android.graphics.Bitmap

data class ValidationResult (
    val isPassed: Boolean,
    val token: String,
    val frame: Bitmap?,
    val failureReason: String?
)
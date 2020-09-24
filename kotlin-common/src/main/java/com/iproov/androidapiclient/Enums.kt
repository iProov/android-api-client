package com.iproov.androidapiclient

enum class ClaimType {
    ENROL, VERIFY
}

enum class PhotoSource(val code: String) {
    ELECTRONIC_ID("eid"),
    OPTICAL_ID("oid")
}


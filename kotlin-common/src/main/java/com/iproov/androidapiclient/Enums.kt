package com.iproov.androidapiclient

enum class ClaimType(val path: String) {
    ENROL("enrol"),
    VERIFY("verify")
}

enum class PhotoSource(val code: String) {
    ELECTRONIC_ID("eid"),
    OPTICAL_ID("oid")
}

enum class AssuranceType(val backendName: String) {
    GENUINE_PRESENCE("genuine_presence"),
    LIVENESS("liveness");
}


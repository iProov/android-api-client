package com.iproov.androidapiclient.kotlinretrofit

import okhttp3.MultipartBody;
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("claim/{claimType}/token")
    suspend fun getAccessToken(@Path("claimType") claimType: String, @Body getTokenRequest: GetTokenRequest): GetTokenResponse

    @Multipart
    @POST("claim/enrol/image")
    suspend fun enrolPhoto(
        @Part("api_key") apiKey: RequestBody,
        @Part("secret") secret: RequestBody,
        @Part("rotation") rotation: RequestBody,
        @Part("token") token: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("source") source: RequestBody
    ): PhotoEnrolResponse

    @POST("claim/{claimType}/validate")
    suspend fun validate(@Path("claimType") claimType: String, @Body validationRequest: ValidationRequest): ValidationResult

    @POST("claim/{token}/invalidate")
    suspend fun invalidate(@Path("token") token: String, @Body invalidationRequest: InvalidationRequest): InvalidationResult
}
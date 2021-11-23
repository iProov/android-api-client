package com.iproov.androidapiclient.kotlinretrofit

import okhttp3.MultipartBody;
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("claim/{claimType}/token")
    fun getAccessToken(@Path("claimType") claimType: String, @Body tokenRequest: TokenRequest): Deferred<Token>

    @Multipart
    @POST("claim/enrol/image")
    fun enrolPhoto(
        @Part("api_key") apiKey: RequestBody,
        @Part("secret") secret: RequestBody,
        @Part("rotation") rotation: RequestBody,
        @Part("token") token: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("source") source: RequestBody
    ): Deferred<Token>

    @POST("claim/verify/validate")
    fun validate(@Body validationRequest: ValidationRequest): Deferred<ValidationResult>

    @POST("claim/{token}/invalidate")
    fun invalidate(@Path("token") token: String, @Body invalidationRequest: InvalidationRequest): Deferred<InvalidationResult>
}
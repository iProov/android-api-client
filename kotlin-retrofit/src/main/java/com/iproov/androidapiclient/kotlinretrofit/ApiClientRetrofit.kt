package com.iproov.androidapiclient.kotlinretrofit

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.gson.GsonBuilder
import com.iproov.androidapiclient.AssuranceType
import com.iproov.androidapiclient.ClaimType
import com.iproov.androidapiclient.DemonstrationPurposesOnly
import com.iproov.androidapiclient.PhotoSource
import com.iproov.androidapiclient.saferUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

/**
 * This code uses Coroutines and Retrofit to demonstrate the iProov ApiService.
 * It is ONLY intended to show how server code might work and we only use this in client code for demo convenience.
 *
 * Not for production!
 */
@DemonstrationPurposesOnly
class ApiClientRetrofit(
    context: Context, // do not make val, if we need to keep a context, keep "context.applicationContext"
    baseUrl: String = "https://eu.rp.secure.iproov.me/api/v2/",
    logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC,
    val apiKey: String,
    val secret: String
) {

    private val api: ApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(converterFactory())
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .baseUrl(baseUrl.saferUrl)
            .build()
            .create(ApiService::class.java)
    }

    private fun converterFactory() : GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapter(GetTokenRequest::class.java, TokenRequestSerializer())
            .create()

        return GsonConverterFactory.create(gson)
    }

    // Transitory access to packageName
    private val appID = context.packageName

    /**
     * Obtain a token, given a ClaimType and userID
     */
    suspend fun getToken(assuranceType: AssuranceType, type: ClaimType, userID: String, riskProfile: String = "", resource: String = appID): GetTokenResponse =
        api.getAccessToken(
            type.toString().lowercase(),
            GetTokenRequest(apiKey, secret, resource, assuranceType.backendName, userID, riskProfile)
        )

    /**
     * Enrol with a Photo, given a token and a PhotoSource
     */
    suspend fun enrolPhoto(token: String, image: Bitmap, source: PhotoSource): PhotoEnrolResponse =
        api.enrolPhoto(
            apiKey.toMultipartRequestBody(),
            secret.toMultipartRequestBody(),
            "0".toMultipartRequestBody(),
            token.toMultipartRequestBody(),
            MultipartBody.Part.createFormData(
                "image",
                "image.jpeg",
                RequestBody.create(
                    "image/jpeg".toMediaTypeOrNull(),
                    image.jpegImageByteArray()
                )
            ),
            source.code.toMultipartRequestBody()
        )

    /**
     * Validate given a token and userID
     */
    suspend fun validate(token: String, claimType: ClaimType, userID: String, riskProfile: String = ""): ValidationResult =
        api.validate(
            claimType.path,
            ValidationRequest(
                apiKey,
                secret,
                userID,
                token,
                riskProfile
            )
        )


    /**
     * Invalidate given a token and reason
     */
    suspend fun invalidate(token: String, reason: String): InvalidationResult =

        api.invalidate(token, InvalidationRequest(reason))

}


// Extensions ----

fun String.toMultipartRequestBody(): RequestBody =

    this.toRequestBody("multipart/form-data".toMediaTypeOrNull())

/**
 * Aggregate extension function to getToken and enrolPhoto in one call.
 * - Get enrol token for the user ID
 * - Enrol the photo against the enrolment token
 * - Get a verify token for the user ID
 */
@DemonstrationPurposesOnly
suspend fun ApiClientRetrofit.enrolPhotoAndGetVerifyToken(userID: String, image: Bitmap, source: PhotoSource, riskProfile: String = ""): String =

    getToken(AssuranceType.GENUINE_PRESENCE, ClaimType.ENROL, userID, riskProfile).token.let { token1 ->
        enrolPhoto(token1, image, source).let {
            getToken(AssuranceType.GENUINE_PRESENCE, ClaimType.VERIFY, userID, riskProfile).token
        }
    }


fun Bitmap.jpegImageByteArray(): ByteArray =

    ByteArrayOutputStream().let { stream ->
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.toByteArray()
    }

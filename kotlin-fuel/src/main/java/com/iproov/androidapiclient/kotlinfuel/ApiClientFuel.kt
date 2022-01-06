package com.iproov.androidapiclient.kotlinfuel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.json.jsonDeserializer
import com.github.kittinunf.result.Result
import org.json.JSONObject
import com.google.gson.Gson
import com.iproov.androidapiclient.AssuranceType
import com.iproov.androidapiclient.ClaimType
import com.iproov.androidapiclient.DemonstrationPurposesOnly
import com.iproov.androidapiclient.PhotoSource
import com.iproov.androidapiclient.merge
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * This code uses Coroutines and Fuel to demonstrate the iProov API.
 * It is ONLY intended to show how server code might work and we only use this in client code for demo convenience.
 *
 * Not for production!
 *
 * Note that in this example callbacks are used to support success and failure lambdas
 * It is equally possible to embrace coroutines further and return to linear code that instead returns or throws,
 * allowing blockingEnrolPhotoAndGetVerifyToken() to become linear code with a try catch instead of callback inside callback
 */
@DemonstrationPurposesOnly
class ApiClientFuel(
    context: Context, // do not make val, if we need to keep a context, keep "context.applicationContext"
    val baseUrl: String = "https://eu.rp.secure.iproov.me/api/v2/",
    val apiKey: String,
    val secret: String
) {

    // Transitory access to packageName
    private val appID = context.packageName

    // Own Fuel instance to prevent access token leakage
    private val fuelInstance = FuelManager()

    /**
     * Obtain a token, given a ClaimType and userID
     */
    @Throws(FuelError::class)
    suspend fun getToken(assuranceType: AssuranceType, type: ClaimType, userID: String, options: Map<String, Any>? = null): String =

        fuelInstance
            .post("${baseUrl.endingWithSlash}claim/${type.toString().toLowerCase()}/token")
            .header("Content-Type" to "application/json")
            .body(Gson().toJson(mapOf(
                "api_key" to apiKey,
                "secret" to secret,
                "resource" to appID,
                "client" to "android",
                "user_id" to userID,
                "assurance_type" to assuranceType.backendName
            ).merge(options)))
            .awaitObjectResult(jsonDeserializer())
            .let { response ->
                when (response) {
                    is Result.Success -> return response.value.obj().getString("token")
                    is Result.Failure -> throw (response.error)
                }
            }

    /**
     * Enrol with a Photo, given a token and a PhotoSource
     */
    @Throws(FuelError::class)
    suspend fun enrolPhoto(token: String, image: Bitmap, source: PhotoSource): String =

        fuelInstance
            .upload("${baseUrl.safelUrl}claim/enrol/image", Method.POST, listOf(
                "api_key" to apiKey,
                "secret" to secret,
                "rotation" to "0",
                "token" to token,
                "source" to source.code
            ))
            .add(BlobDataPart(image.jpegImageStream(), "image", filename = "image.jpeg", contentType="image/jpeg"))
            .header("Content-Type" to "multipart/form-data; boundary=-------kjqdgfljhsgdfljhgsdlfjhgasdf" )
            .awaitObjectResult(jsonDeserializer())
            .let { response ->
                when (response) {
                    is Result.Success -> return response.value.obj().getString("token")
                    is Result.Failure -> throw (response.error)
                }
            }


    /**
     * Validate given a token and userID
     */
    @Throws(FuelError::class)
    suspend fun validate(token: String, userID: String): ValidationResult =

        fuelInstance
            .post("${baseUrl.safelUrl}claim/verify/validate")
            .body(Gson().toJson(mapOf(
                "api_key" to apiKey,
                "secret" to secret,
                "user_id" to userID,
                "token" to token,
                "ip" to "127.0.0.1",
                "client" to "android"
            )))
            .awaitObjectResult(jsonDeserializer())
            .let { response ->
                when (response) {
                    is Result.Success -> return response.value.obj().toValidationResult()
                    is Result.Failure -> throw (response.error)
                }
            }

    /**
     * Invalidate given a token and reason
     */
    @Throws(FuelError::class)
    suspend fun invalidate(token: String, reason: String): InvalidationResult =

        fuelInstance
            .post("${baseUrl.safelUrl}claim/$token/invalidate")
            .body(Gson().toJson(mapOf(
//                "api_key" to apiKey,
//                "secret" to secret,
                "reason" to reason
            )))
            .awaitObjectResult(jsonDeserializer())
            .let { response ->
                when (response) {
                    is Result.Success -> return response.value.obj().toInvalidationResult()
                    is Result.Failure -> throw (response.error)
                }
            }
}


// Extensions ----

/**
 * Aggregate extension function to getToken and enrolPhoto in one call.
 * - Get enrol token for the user ID
 * - Enrol the photo against the enrolment token
 * - Get a verify token for the user ID
 */
@DemonstrationPurposesOnly
suspend fun ApiClientFuel.enrolPhotoAndGetVerifyToken(userID: String, image: Bitmap, assuranceType: AssuranceType, source: PhotoSource, options: Map<String, Any>? = null): String =

    getToken(assuranceType, ClaimType.ENROL, userID).let { token1 ->
        enrolPhoto(token1, image, source)
        getToken(assuranceType, ClaimType.VERIFY, userID, options)
    }

fun Bitmap.jpegImageStream(): InputStream =

    ByteArrayOutputStream().let { stream ->
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        ByteArrayInputStream(stream.toByteArray())
    }


/**
 * JSON to ValidationResult mapping
 */
fun JSONObject.toValidationResult(): ValidationResult =

    ValidationResult(
        this.getBoolean("passed"),
        this.getString("token"),
        this.getOrNullString("frame")?.base64DecodeBitmap(),
        (this.opt("result") as? JSONObject)?.getOrNullString("reason")
    )

/**
 * JSON to InvalidationResult mapping
 */
fun JSONObject.toInvalidationResult(): InvalidationResult =

    InvalidationResult(
        this.getBoolean("claim_aborted"),
        this.getBoolean("user_informed")
    )

private inline val String.endingWithSlash: String
    get() = if (endsWith("/")) this else "$this/"

private inline val String.safelUrl: String
    get() = if (endsWith("api/v2/")) endingWithSlash else "${endingWithSlash}api/v2/"

/**
 * Base64 decode to Bitmap mapping
 */
fun String.base64DecodeBitmap(): Bitmap? =

    with(Base64.decode(this, Base64.DEFAULT)) { try { BitmapFactory.decodeByteArray(this, 0, this.size) } catch(ex: Exception) { null } }

/**
 * Helper JSON function
 */
fun JSONObject.getOrNullString(key: String): String? =
    try {
        this.getString(key).let { str -> if (str.isEmpty()) null else str }
    } catch (e: java.lang.Exception) {
        null
    }


package com.iproov.androidapiclient.harness

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.iproov.androidapiclient.*
import com.iproov.androidapiclient.kotlinfuel.ApiClientFuel
import com.iproov.androidapiclient.kotlinfuel.enrolPhotoAndGetVerifyToken
import com.iproov.androidapiclient.kotlinretrofit.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException

class DemoActivity : AppCompatActivity() {

    private val uiSupervisorJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + uiSupervisorJob)

    @DemonstrationPurposesOnly
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        demoKotlinRetrofitApiCalls()

        demoKotlinFuelApiCalls()
    }

    override fun onDestroy() {
        super.onDestroy()
        uiScope.coroutineContext.cancelChildren()
    }

    private fun report(msg: String) {
        if (textView.text.isNotEmpty())
            textView.text = textView.text.toString() + "\n"
        textView.text = textView.text.toString() + msg
    }

    private fun demoKotlinRetrofitApiCalls() {
        report("Start Kotlin Retrofit")

        val jsonStr = "secrets.json".jsonFile(this)
        val json = JSONObject(jsonStr ?: "{}")

        val apiKey: String = json.optString("api_key")
        val secret: String = json.optString("secret")

        val apiClient = ApiClientRetrofit(
            context = this,
            baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
            logLevel = okhttp3.logging.HttpLoggingInterceptor.Level.BODY,
            apiKey = apiKey,
            secret = secret
        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.richard)

        /**
         * NOTE THIS MUST BE DIFFERENT EACH TIME
         */
        val userID = "${"retrofitdemo".datetime()}@example.com"

        uiScope.launch {
            try {
                val token = withContext(Dispatchers.IO) {
                    apiClient.enrolPhotoAndGetVerifyToken(userID, bitmap, PhotoSource.OPTICAL_ID)
                }
                Log.i("Main", "success (Retrofit) = $token")
                report("success (Retrofit) = $token")

                val userID2 = "${"retrofitdemo".datetime()}@example.com"
                val tokenInvalidating = withContext(Dispatchers.IO) {
                    apiClient.getToken(AssuranceType.GENUINE_PRESENCE, com.iproov.androidapiclient.ClaimType.ENROL, userID2)
                }
                val invalidateResult = apiClient.invalidate(tokenInvalidating.token, "Test reason")
                Log.i("Main", "success (Retrofit, invalidate) = $tokenInvalidating claimAborted=${invalidateResult.claimAborted} userInformed=${invalidateResult.userInformed}")
                report("success (Retrofit, invalidate) = $tokenInvalidating claimAborted=${invalidateResult.claimAborted} userInformed=${invalidateResult.userInformed}")

            } catch (httpEx: HttpException) {
                httpEx.printStackTrace()
                Log.e("Main", "failure (Kotlin Retrofit) $httpEx")
                report("failure (Kotlin Retrofit) $httpEx")
            }
        }
    }

    private fun demoKotlinFuelApiCalls() {
        report("Start Kotlin Fuel")

        val jsonStr = "secrets.json".jsonFile(this)
        val json = JSONObject(jsonStr ?: "{}")

        val apiKey = json.optString("api_key")
        val secret = json.optString("secret")

        val apiClient = ApiClientFuel(
            context = this,
            baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
            apiKey = apiKey,
            secret = secret
        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.richard)

        /**
         * NOTE THIS MUST BE DIFFERENT EACH TIME
         */
        val userID = "${"fueldemo".datetime()}@example.com"

        uiScope.launch {
            try {
                val token = withContext(Dispatchers.IO) {
                    apiClient.enrolPhotoAndGetVerifyToken(userID, bitmap, AssuranceType.GENUINE_PRESENCE, PhotoSource.OPTICAL_ID)
                }
                Log.i("Main", "success (Fuel) = $token")
                report("success (Kotlin Fuel) = $token")

                val userID2 = "${"fueldemo".datetime()}@example.com"
                val tokenInvalidating = withContext(Dispatchers.IO) {
                    apiClient.getToken(AssuranceType.GENUINE_PRESENCE, com.iproov.androidapiclient.ClaimType.ENROL, userID2)
                }
                val invalidateResult = apiClient.invalidate(tokenInvalidating, "Test reason")
                Log.i("Main", "success (Kotlin Fuel, invalidate) = $tokenInvalidating claimAborted=${invalidateResult.claimAborted} userInformed=${invalidateResult.userInformed}")
                report("success (Kotlin Fuel, invalidate) = $tokenInvalidating claimAborted=${invalidateResult.claimAborted} userInformed=${invalidateResult.userInformed}")

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.w("Main", "failure (Kotlin Fuel) $ex")
                report("failure (Kotlin Fuel) $ex")
            }
        }
    }
}

package com.iproov.androidapiclient.harness

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.iproov.androidapiclient.*
import com.iproov.androidapiclient.javaretrofit.ApiClientJavaRetrofit
import com.iproov.androidapiclient.javaretrofit.Token
import com.iproov.androidapiclient.kotlinfuel.ApiClientFuel
import com.iproov.androidapiclient.kotlinfuel.enrolPhotoAndGetVerifyToken
import com.iproov.androidapiclient.kotlinretrofit.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response

class DemoActivity : AppCompatActivity() {

    private val uiSupervisorJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + uiSupervisorJob)

    @DemonstrationPurposesOnly
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        demoJavaRetrofitCallbacks()

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

    private fun demoJavaRetrofitCallbacks() {
        report("Start Java Retrofit")

        val jsonStr = "secrets.json".jsonFile(this)
        val json = JSONObject(jsonStr ?: "{}")

        val clientid = json.optString("clientid")
        val secret = json.optString("secret")
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.richard)

        /**
         * NOTE THIS MUST BE DIFFERENT EACH TIME
         */
        val userID = "${"retrofitdemo".datetime()}@example.com"

        val apiClient = ApiClientJavaRetrofit(
            this,
            "https://eu.rp.secure.iproov.me/api/v2/",
            okhttp3.logging.HttpLoggingInterceptor.Level.BODY,
            clientid,
            secret
        )

        apiClient.enrolPhotoAndGetVerifyToken(
            userID,
            bitmap,
            com.iproov.androidapiclient.javaretrofit.PhotoSource.OPTICAL_ID,
            { _: Call<Token>, response: Response<Token> ->
                Log.i(
                    "Main",
                    "success (Java Retrofit) = ${response.body()?.token}"
                )
                report("success (Java Retrofit) = ${response.body()?.token}")
            },
            {
                Log.e("Main", "failure (Java Retrofit) $it")
                report("failure (Java Retrofit) = $it")
            }
        )

    }

    private fun demoKotlinRetrofitApiCalls() {
        report("Start Kotlin Retrofit")

        val jsonStr = "secrets.json".jsonFile(this)
        val json = JSONObject(jsonStr ?: "{}")

        val clientid: String = json.optString("clientid")
        val secret: String = json.optString("secret")

        val apiClient = ApiClientRetrofit(
            context = this,
            baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
            logLevel = okhttp3.logging.HttpLoggingInterceptor.Level.BODY,
            apiKey = clientid,
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

        val clientid = json.optString("clientid")
        val secret = json.optString("secret")

        val apiClient = ApiClientFuel(
            context = this,
            baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
            apiKey = clientid,
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
                    apiClient.enrolPhotoAndGetVerifyToken(userID, bitmap, PhotoSource.OPTICAL_ID)
                }
                Log.i("Main", "success (Fuel) = $token")
                report("success (Kotlin Fuel) = $token")
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.w("Main", "failure (Kotlin Fuel) $ex")
                report("failure (Kotlin Fuel) $ex")
            }
        }
    }
}

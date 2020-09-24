package com.iproov.androidapiclient.harness

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.iproov.androidapiclient.*
import com.iproov.androidapiclient.kotlinfuel.ApiClientFuel
import com.iproov.androidapiclient.kotlinfuel.enrolPhotoAndGetVerifyToken
import com.iproov.androidapiclient.kotlinretrofit.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import org.json.JSONObject
import retrofit2.HttpException

class DemoActivity : AppCompatActivity() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    @DemonstrationPurposesOnly
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        demoJavaRetrofitCalls()

        demoRetrofitApiCalls()

        demoFuelApiCalls()
    }

    private fun demoJavaRetrofitCalls() {
        val jsonStr = "secrets.json".jsonFile(this)
        val json = JSONObject(jsonStr ?: "{}")

        val clientid = json.optString("clientid")
        val secret = json.optString("secret")
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.richard)

        /**
         * NOTE THIS MUST BE DIFFERENT EACH TIME
         */
        val userID = "${"retrofitdemo".datetime()}@example.com"

        DemoJavaRetrofit.enrolPhotoAndGetVerifyToken(
            this,
            userID,
            bitmap,
            com.iproov.androidapiclient.javaretrofit.PhotoSource.OPTICAL_ID,
            "https://eu.rp.secure.iproov.me/api/v2/",
            okhttp3.logging.HttpLoggingInterceptor.Level.BODY,
            clientid,
            secret
        )
    }

    private fun demoRetrofitApiCalls() {

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

        uiScope.launch(Dispatchers.IO) {

            try {
                val token = apiClient.enrolPhotoAndGetVerifyToken(userID, bitmap, PhotoSource.OPTICAL_ID)
                Log.i("Main", "success (Retrofit) = $token")

            } catch (httpEx: HttpException) {

                httpEx.printStackTrace()
                Log.e("Main", "failure $httpEx")
            }
        }
    }

    private fun demoFuelApiCalls() {
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

        uiScope.launch(Dispatchers.IO) {
            try {
                val token = apiClient.enrolPhotoAndGetVerifyToken(userID, bitmap, PhotoSource.OPTICAL_ID)
                Log.i("Main", "success (Fuel) = $token")

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    ex.printStackTrace()
                    Log.w("Main", "get/enrol exception $ex")
                }
            }
        }
    }
}

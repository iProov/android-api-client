package com.iproov.androidapiclient.harness;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.iproov.androidapiclient.javaretrofit.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;

public class DemoJavaRetrofit {

    public static void enrolPhotoAndGetVerifyToken(
        Context context,
        String userID,
        Bitmap bitmap,
        PhotoSource photoSource,
        String baseUrl,
        HttpLoggingInterceptor.Level logLevel,
        String apiKey,
        String secret
    ) {
        ApiClientJavaRetrofit apiClient = new ApiClientJavaRetrofit(context, baseUrl, logLevel, apiKey, secret);

        apiClient.enrolPhotoAndGetVerifyToken(
            userID,
            bitmap,
            photoSource,
            (Call<Token> call, Response<Token> response) -> {
                Log.i("Main", "success (Java Retrofit) = ${response.getBody().getToken()}");
            },
            (Throwable t) -> {
                Log.e("Main", "failure (Java Retrofit) $t");
            }
        );
    }
}

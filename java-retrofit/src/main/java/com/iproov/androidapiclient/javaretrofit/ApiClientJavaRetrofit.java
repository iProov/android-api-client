package com.iproov.androidapiclient.javaretrofit;


import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@DemonstrationPurposeOnly
public class ApiClientJavaRetrofit {

    private String appID;

    private String apiKey;
    private String secret;

    private ApiJavaService apiJavaService;

    public ApiClientJavaRetrofit(
            Context context,
            String baseUrl,
            HttpLoggingInterceptor.Level logLevel,
            String apiKey,
            String secret
    ) {

        this.apiKey = apiKey;
        this.secret = secret;

        this.appID = context.getPackageName();

        this.apiJavaService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory())
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(
                                        new HttpLoggingInterceptor()
                                                .setLevel(logLevel == null ? HttpLoggingInterceptor.Level.BASIC : logLevel)
                                )
                                .build())
                .build()
                .create(ApiJavaService.class);
    }

    private static GsonConverterFactory gsonConverterFactory() {
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(TokenRequest.class, new TokenRequestSerializer());
        return GsonConverterFactory.create(builder.create());
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    private Call<Token> doGetToken(
            AssuranceType assuranceType,
            ClaimType claimType,
            String userID,
            Map<String, Object> options
    ) {

        return this.apiJavaService.getAccessToken(
                claimType.toString().toLowerCase(),
                new TokenRequest(
                        this.apiKey,
                        this.secret,
                        this.appID,
                        userID,
                        "android",
                        assuranceType,
                        options
                )
        );
    }

    private byte[] jpegImageByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void getToken(
            ClaimType claimType,
            String userID,
            Callback<Token> callback,
            Map<String, Object> options
    ) {
        doGetToken(AssuranceType.GENUINE_PRESENCE, claimType, userID, options).enqueue(callback);
    }


    public void getToken(
            AssuranceType assuranceType,
            ClaimType claimType,
            String userID,
            Callback<Token> callback,
            Map<String, Object> options
    ) {
        doGetToken(assuranceType, claimType, userID, options).enqueue(callback);
    }

    public void getToken(
            AssuranceType assuranceType,
            ClaimType claimType,
            String userID,
            final CallbackResponse<Token> callbackResponse,
            final CallbackFailure callbackFailure,
            Map<String, Object> options
    ) {

        doGetToken(assuranceType, claimType, userID, options).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                callbackResponse.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                callbackFailure.onFailure(t);
            }
        });
    }

    public Call<Token> doEnrolPhoto(
            String token,
            Bitmap image,
            PhotoSource source
    ) {

        return this.apiJavaService.enrolPhoto(
                toRequestBody(this.apiKey),
                toRequestBody(this.secret),
                toRequestBody("0"),
                toRequestBody(token),
                MultipartBody.Part.createFormData(
                        "image",
                        "image.jpeg",
                        RequestBody.create(
                                MediaType.parse("image/jpeg"),
                                jpegImageByteArray(image)
                        )
                ),
                toRequestBody(source.getName())
        );
    }

    public void enrolPhoto(
            String token,
            Bitmap image,
            PhotoSource source,
            Callback<Token> callback
    ) {

        doEnrolPhoto(token, image, source).enqueue(callback);
    }

    public void enrolPhoto(
            String token,
            Bitmap image,
            PhotoSource source,
            final CallbackResponse<Token> callbackResponse,
            final CallbackFailure callbackFailure
    ) {

        doEnrolPhoto(token, image, source).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                callbackResponse.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                callbackFailure.onFailure(t);
            }
        });
    }

    public Call<ValidationResult> doValidate(String token, String userID, Callback<ValidationResult> callback) {

        return this.apiJavaService.validate(
                new ValidationRequest(
                        this.apiKey,
                        this.secret,
                        userID,
                        token,
                        "127.0.0.1",
                        "android"
                )
        );
    }

    public void validate(String token, String userID, Callback<ValidationResult> callback) {

        doValidate(token, userID, callback);
    }

    public void validate(
            String token,
            String userID,
            final CallbackResponse<ValidationResult> callbackResponse,
            final CallbackFailure callbackFailure
    ) {

        doValidate(token, userID, new Callback<ValidationResult>() {
            @Override
            public void onResponse(Call<ValidationResult> call, Response<ValidationResult> response) {
                callbackResponse.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ValidationResult> call, Throwable t) {
                callbackFailure.onFailure(t);
            }
        });
    }

    public void enrolPhotoAndGetVerifyToken(
            String userID,
            Bitmap image,
            PhotoSource source,
            final CallbackResponse<Token> callbackResponse,
            final CallbackFailure callbackFailure,
            Map<String, Object> options
    ) {
        try {
            getToken(
                    AssuranceType.GENUINE_PRESENCE,
                    ClaimType.ENROL,
                    userID,
                    (Call<Token> call, Response<Token> response) -> {
                        if (response.body() != null)
                            enrolPhoto(
                                    response.body().getToken(),
                                    image,
                                    source,
                                    (Call<Token> call2, Response<Token> response2) ->
                                            getToken(
                                                    AssuranceType.GENUINE_PRESENCE,
                                                    ClaimType.VERIFY,
                                                    userID,
                                                    callbackResponse,
                                                    callbackFailure,
                                                    options
                                            ),
                                    callbackFailure
                            );
                        else
                            callbackFailure.onFailure(new NullPointerException());
                    },
                    callbackFailure,
                    null
            );
        } catch (Exception ex) {
            callbackFailure.onFailure(ex);
        }
    }

    public enum AssuranceType {
        GENUINE_PRESENCE("genuine_presence"),
        LIVENESS("liveness");

        public final String backendName;

        AssuranceType(String backendName) {
            this.backendName = backendName;
        }
    }
}
package com.iproov.androidapiclient.javaretrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiJavaService {

    @POST("claim/{claimType}/token")
    Call<Token> getAccessToken(@Path("claimType") String claimType, @Body TokenRequest tokenRequest);

    @Multipart
    @POST("claim/enrol/image")
    Call<Token> enrolPhoto(
            @Part("api_key") RequestBody apiKey,
            @Part("secret") RequestBody secret,
            @Part("rotation") RequestBody rotation,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part image,
            @Part("source") RequestBody source
    );

    @POST("claim/verify/validate")
    Call<ValidationResult> validate(@Body ValidationRequest validationRequest);

    @POST("claim/{token}/invalidate")
    Call<InvalidationResult> invalidate(@Path("token") String token, @Body InvalidationRequest invalidationRequest);
}

package com.iproov.androidapiclient.kotlinretrofit

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class TokenRequestSerializer : JsonSerializer<GetTokenRequest> {

    override fun serialize(src: GetTokenRequest?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonObject().apply {
            // TODO This isn't scalable but using context.serialize with src results in an infinite loop
            addProperty("api_key", src!!.apiKey)
            addProperty("secret", src.secret)
            addProperty("resource", src.resource)
            addProperty("assurance_type", src.assuranceType)
            addProperty("user_id", src.userId)
            addProperty("riskProfile", src.riskProfile)
        }
}
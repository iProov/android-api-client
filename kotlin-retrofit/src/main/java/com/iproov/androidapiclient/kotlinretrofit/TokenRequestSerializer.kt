package com.iproov.androidapiclient.kotlinretrofit

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class TokenRequestSerializer : JsonSerializer<TokenRequest> {

    override fun serialize(src: TokenRequest?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {

        val jsonObject = JsonObject().apply {
            // TODO This isn't scalable but using context.serialize with src results in an infinite loop
            addProperty("api_key", src!!.apiKey)
            addProperty("secret", src.secret)
            addProperty("resource", src.resource)
            addProperty("user_id", src.userId)
            addProperty("client", src.client)
            addProperty("assurance_type", src.assuranceType)
        }

        src!!.options?.let {
            context!!.serialize(it).asJsonObject.entrySet().forEach{ entry -> jsonObject.add(entry.key, entry.value) }
        }

        return jsonObject
    }

}
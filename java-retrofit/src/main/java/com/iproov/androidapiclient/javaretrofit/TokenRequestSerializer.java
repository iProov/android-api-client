package com.iproov.androidapiclient.javaretrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;
/*
    This is only used to inline the options hashmap, gson default behaviour is to nest
 */
public class TokenRequestSerializer implements JsonSerializer<TokenRequest> {

    @Override
    public JsonElement serialize(TokenRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // TODO This isn't scalable but using context.serialize with src results in an infinite loop
        jsonObject.addProperty("api_key", src.getApiKey());
        jsonObject.addProperty("secret", src.getSecret());
        jsonObject.addProperty("resource", src.getResource());
        jsonObject.addProperty("user_id", src.getUserId());
        jsonObject.addProperty("client", src.getClient());
        jsonObject.addProperty("assurance_type", src.getAssuranceType());

        Map<String, Object> options = src.getOptions();

        if (options == null)
            return jsonObject;

        JsonObject jsonOptions = context.serialize(options).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonOptions.entrySet()) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }

        return jsonObject;
    }
}

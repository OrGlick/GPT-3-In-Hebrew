package com.example.gpt3inhebrew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Translate
{
    String lang;

    public Translate(String lang)
    {
        this.lang = lang;
    }

    // This function performs a POST request.
    public String Post(String textToTranslate) throws IOException
    {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", lang)
                .build();

        // Instantiates the OkHttpClient.
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("text", textToTranslate);
            String text = "[" + jsonObject + "]";
            RequestBody body = RequestBody.create(text, mediaType);

            String subscriptionKey = Helper.azure_translation_api_key;
            String location = Helper.azure_translation_resource_location;

            Request request = new Request.Builder().url(url).post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                    .addHeader("Ocp-Apim-Subscription-Region", location)
                    .addHeader("Content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            JSONArray jsonArray = new JSONArray(response.body().string());
            JSONArray jsonArray1 = (JSONArray) jsonArray.getJSONObject(0).get("translations");
            JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
            return jsonObject1.get("text").toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}



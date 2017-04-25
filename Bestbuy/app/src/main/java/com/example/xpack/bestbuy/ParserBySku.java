package com.example.xpack.bestbuy;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xpack on 16/04/17.
 */

public class ParserBySku {


    private static String url = "";
    private static OkHttpClient http;
    private static JSONObject json = null;
    public static int TotalPages=0;
    private static Context context = null;



    private static JSONObject getJSON(String Url) throws IOException, JSONException {

        Request request = new Request.Builder().url(Url).build();

            http = new OkHttpClient
                    .Builder()
                    .build();


        Response response = http.newCall(request).execute();

        json = new JSONObject(response.body().string());

        return json;
    }

    public static ArrayList<Products> getProducts(ArrayList<Products> produits, String Url) throws IOException, JSONException {

        JSONObject json = getJSON(Url);





        //Products[] produits = new Products[products.length()];
        // ArrayList<Products> produits = new ArrayList<Products>(products.length());



            produits.add(new Products(
                    json.getString("salePrice"),
                    json.getString("name"),
                    json.getString("mediumImage"),
                    json.getString("customerReviewAverage"),
                    json.getString("largeFrontImage"),
                    json.getString("onlineAvailabilityUpdateDate"),
                    json.getString("customerReviewCount"),
                    json.getString("onlineAvailability"),
                    json.getString("longDescription"),
                    json.getString("addToCartUrl"),
                    json.getString("mediumImage"),
                    json.getString("largeImage"),
                    json.getString("sku"),
                    json.getJSONArray("details"),
                    json.getJSONArray("crew"),
                    json.getJSONArray("cast"),
                    json.getString("regularPrice"),
                    json.getString("dollarSavings"),
                    json.getString("url")
            ));





        return  produits;
    }


}

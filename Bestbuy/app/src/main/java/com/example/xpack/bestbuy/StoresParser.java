package com.example.xpack.bestbuy;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by xpack on 18/03/17.
 */

public class StoresParser {


    // private static String url = "";
    private static OkHttpClient http;
    private static JSONObject json = null;
    public static int TotalPages=0;
    private static Context context = null;


    private static JSONObject getJSON(String Url) throws IOException, JSONException {

        Request request = new Request.Builder().url(Url).build();

        if (http == null){

            http = new OkHttpClient
                    .Builder()
                    .build();
        }

        Response response = http.newCall(request).execute();

        json = new JSONObject(response.body().string());

        return json;
    }

    public static ArrayList<Stores> getStores(ArrayList<Stores> produits,String Url) throws IOException, JSONException {

        JSONObject json = getJSON(Url);
        JSONArray stores = json.getJSONArray("stores");



        //Products[] produits = new Products[products.length()];
        // ArrayList<Products> produits = new ArrayList<Products>(products.length());

        for (int i=0;i<stores.length();i++){
            JSONObject prod = stores.getJSONObject(i);
            produits.add(new Stores(
                    prod.getString("name"),
                    prod.getString("longName"),
                    prod.getString("address"),
                    prod.getString("address2"),
                    prod.getJSONArray("services"),
                    prod.getJSONArray("detailedHours"),
                    prod.getString("phone"),
                    prod.getString("city"),
                    prod.getString("fullPostalCode"),
                    prod.getString("hoursAmPm")
            ));
        }




        return  produits;
    }
}
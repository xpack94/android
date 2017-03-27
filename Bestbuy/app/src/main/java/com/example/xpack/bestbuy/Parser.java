package com.example.xpack.bestbuy;

import android.util.Log;

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

public class Parser {


   // private static String url = "";
    private static OkHttpClient http;
    private static JSONObject json = null;

    private static JSONObject getJSON(String Url) throws IOException, JSONException {


        Request request = new Request.Builder().url(Url).build();

        if (http == null)
            http = new OkHttpClient();

        Response response = http.newCall(request).execute();

        json = new JSONObject(response.body().string());

        return json;
    }

    public static ArrayList<Products> getProducts(ArrayList<Products> produits,String Url) throws IOException, JSONException {

        JSONObject json = getJSON(Url);
        JSONArray products = json.getJSONArray("products");




        //Products[] produits = new Products[products.length()];
       // ArrayList<Products> produits = new ArrayList<Products>(products.length());

        for (int i=0;i<products.length();i++){
            JSONObject prod = products.getJSONObject(i);
            Log.d("t", ""+prod.getString("name"));
            produits.add(new Products(
                    prod.getString("salePrice")+"$",
                    prod.getString("name"),
                    prod.getString("mediumImage")
            ));
        }




        return  produits;
    }
}
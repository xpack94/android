package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by xpack on 02/04/17.
 */

public class TvHomeTheater extends AppCompatActivity {



    ListView list;
    int page=1;
    ProgressDialog pDialog;
    AllProducts.itemsAdapter mAdapter;
    int pos=1;
    String url1="https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page=";
    String url2="&apiKey=tghcgc6qnf72tat8a5kbja9r";

    ArrayList<Products> produits=new ArrayList<Products>();
    ProgressDialog progress ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        Intent intent=getIntent();
        progress=new ProgressDialog(this);
        list= (ListView) findViewById(R.id.list);
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
    }
}

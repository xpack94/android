package com.example.xpack.bestbuy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xpack on 24/04/17.
 */

public class StoreDetails extends Activity  implements Serializable,NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    SearchView search;
    String url1 = "https://api.bestbuy.com/v1/stores?format=json&show=all&pageSize=25&page=";
    String url2 = "&apiKey=tghcgc6qnf72tat8a5kbja9r";
    ProgressDialog progress;
    ImageView logo;
    ArrayList<Stores> stores = new ArrayList<Stores>();
    int page;
    int position;
    ImageView share;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_store);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//
//        View header = (View) navigationView.getHeaderView(0);
//        ImageView v = (ImageView) header.findViewById(R.id.imageView);
//
//
//        final ImageView toggler = (ImageView) findViewById(R.id.toggler);
//        toggler.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });
        progress = new ProgressDialog(this);
        Intent intent=getIntent();
         page=intent.getExtras().getInt("page");
         position=intent.getExtras().getInt("position");

        logo=(ImageView) findViewById(R.id.logo);
        Picasso.with(getApplicationContext())
                .load("http://www.userlogos.org/files/logos/mafi0z/BestBuy.png")
                .into(logo);
        search=(SearchView) findViewById(R.id.searchView);
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setVisibility(View.GONE);
//                toggler.setVisibility(View.GONE);



            }
        });
        //reafficher le logo quand on quite la recherche
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logo.setVisibility(View.VISIBLE);
//                toggler.setVisibility(View.VISIBLE);
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //si l'utilisateur tape un nom de produit a rechercher
                //appeler l'activité AllProducts qui a son tours s'occupe de chercher
                //le nom du produit tapé par l'utilisateur
                Intent intent = new Intent(StoreDetails.this, AllProducts.class);
                String ur = "https://api.bestbuy.com/v1/products(search=";
                String word = "";
                Boolean word_found = false;
                int i = 0;
                while (i < query.length()) {
                    Log.e("t", "the inde is  " + i);
                    while ((i < query.length()) && (query.charAt(i) != ' ')) {

                        word += query.charAt(i);
                        word_found = true;
                        i++;
                        Log.e("t", "index:" + i + " word = " + word);
                        // Log.e("t", "the word is  "+word );

                    }

                    if (word_found) {
                        word += " ";
                        word_found = false;
                    }

                    i++;
                }
                ur += word.replaceAll(" ", "&search=") + "*)?format=json&shwo=all&pageSize=25&page=";
                intent.putExtra("url1", ur);
                intent.putExtra("url2", "&apiKey=tghcgc6qnf72tat8a5kbja9r");
                intent.putExtra("page", 1);
                intent.putExtra("decalage", 0);
                intent.putExtra("title", getResources().getString(R.string.search_results));
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        share=(ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Your body here";
                String shareSub = "Your subject here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        Fetcher f=new Fetcher();
        f.execute();

    }



    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Stores>> {


        public void onPreExecute() {


            progress.setCancelable(false);
            progress.setTitle("" + getResources().getString(R.string.loadingStores));
            progress.show();
        }

        @Override
        protected ArrayList<Stores> doInBackground(Object... params) {


            try {

                for (int i=1;i<=page;i++){
                    StoresParser.getStores(stores, url1 + i + url2);
                }



                //verifier si on est rendu au dernier produit
                //si oui le boutton load More deviendra invisible


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return stores;
        }

        @Override
        protected void onPostExecute(final ArrayList<Stores> prods) {
            progress.hide();

            TextView name=(TextView) findViewById(R.id.storeName);
            TextView address1=(TextView) findViewById(R.id.address1);
            TextView address2=(TextView) findViewById(R.id.address2);
            TextView phone=(TextView) findViewById(R.id.phone);
            TextView city=(TextView) findViewById(R.id.city);
            TextView postalCode=(TextView) findViewById(R.id.postalCode);
            TextView hours=(TextView) findViewById(R.id.hours);
            TextView offers=(TextView) findViewById(R.id.services);

            name.setText(prods.get(position).longName);
            address1.setText(prods.get(position).address1);
            address2.setText(prods.get(position).addres2);
            phone.setText(prods.get(position).phone);
            city.setText(prods.get(position).city);
            postalCode.setText(prods.get(position).postalCode);
            String h=prods.get(position).hoursAmPm;
            h=h.replaceAll(";","\n");
            int index=h.indexOf("Sun",5);
            h=h.substring(0,index);
            hours.setText(h);


            JSONArray services=prods.get(position).services;
            String service="";

            for(int i=0;i<services.length();i++){

                try {
                    JSONObject j=services.getJSONObject(i);
                    service+=j.getString("service")+"\n";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            offers.setText(service);


        }


    }











    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
    return false;
    }
}
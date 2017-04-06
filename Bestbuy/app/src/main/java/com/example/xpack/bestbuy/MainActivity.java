package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Fragment {


    ListView liste;
    LayoutInflater inflater;
    ProgressDialog progress;
    LinearLayout inLay, inLay2, inLay3, inLay4, inLay5;
    int page = 2;
    String[] categories = new String[2];
    int line = 1;
    String url1 = "https://api.bestbuy.com/v1/products(categoryPath.id=";
    String url2 = "*)?format=json&show=all&pageSize=25&page=";
    String url3 = "&apiKey=tghcgc6qnf72tat8a5kbja9r";
    int cat = 0;
    ArrayList<Products> produits1 = new ArrayList<Products>();
    ArrayList<Products> produits2 = new ArrayList<Products>();
    ArrayList<Products> produits3 = new ArrayList<Products>();
    ArrayList<Products> produits4 = new ArrayList<Products>();
    ArrayList<Products> produits5 = new ArrayList<Products>();
    TextView tv_theater, com_tab, mobiles, videoGames, movies_music;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.activity_main, container, false);



        //tester si le telephone est connecté a internet
        //afficher une boite de dialogue sinon
        if(!haveNetworkConnection()){
            NoInternet ni =new NoInternet();
            ni.show(getActivity().getFragmentManager(),"dialog");
            //creer un thread qui attend que le button ok soit appuyer pour faire appele a la methode load
            //afin de charger les données
            new Thread(){
                public void run(){
                    while (!NoInternet.clicked && !haveNetworkConnection()){

                    }
                    try {
                        Thread.sleep(7000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                load();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                }
            }.start();



        }else{
            load();
        }



        return v;
    }




        //la methode load permet de charger les données dans l'activité principale

    public void load() {


        inflater = getActivity().getLayoutInflater();
        inLay = (LinearLayout)  v.findViewById(R.id.innerLay);
        inLay2 = (LinearLayout) v.findViewById(R.id.innerLay2);
        inLay3 = (LinearLayout) v.findViewById(R.id.innerLay3);
        inLay4 = (LinearLayout) v.findViewById(R.id.innerLay4);
        inLay5 = (LinearLayout) v.findViewById(R.id.innerLay5);
        tv_theater = (TextView) v.findViewById(R.id.tv_theater);
        com_tab = (TextView)    v.findViewById(R.id.Computer_Tablets);
        mobiles = (TextView)    v.findViewById(R.id.mobiles);
        videoGames = (TextView) v.findViewById(R.id.video_games);
        movies_music = (TextView) v.findViewById(R.id.Movies_Music);

        progress = new ProgressDialog(getActivity());


        //movies and music
        Fetcher f = new Fetcher(1, url1, url2, "abcat0600000", produits3);
        //video games
        Fetcher j = new Fetcher(2, url1, url2, "abcat0700000", produits4);
        //mobiles
        Fetcher w = new Fetcher(3, url1, url2, "abcat0800000", produits5);
        //computers and tablets
        Fetcher k = new Fetcher(4, url1, url2, "abcat0500000", produits2);
        //tv and home theater
        Fetcher l = new Fetcher(5, url1, url2, "abcat0100000", produits1);

        l.execute();
        k.execute();
        f.execute();
        j.execute();
        w.execute();

        tv_theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetails(view);
            }
        });
        com_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetails(view);
            }
        });

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetails(view);
            }

        });
        movies_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetails(view);
            }
        });

        videoGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetails(view);
            }
        });


    }


    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Products>> {

        int number;
        String url1;
        String url2;
        String id;
        ArrayList<Products> produits;

        Fetcher(int number, String url1, String url2, String id, ArrayList<Products> produits) {
            this.number = number;
            this.url1 = url1;
            this.url2 = url2;
            this.id = id;
            this.produits = produits;
        }


        public void onPreExecute() {
            progress.setCancelable(false);
            progress.setTitle("loading...");
            progress.show();
        }


        @Override
        protected ArrayList<Products> doInBackground(Object... params) {


            try {


                Parser.getProducts(this.produits, url1 + id + url2 + page + url3);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return produits;
        }

        @Override
        protected void onPostExecute(final ArrayList<Products> prods) {
            progress.hide();
            if (number == 5) {
                tv_theater.setText(getResources().getString(R.string.tv_theater) + ">");
                tv_theater.setTag("tv_theater");
                tv_theater.setTag(R.string.tag, "abcat0100000");
                for (int x = 0; x < 25; x++) {
                    inLay.addView(getView(x, prods, this.id));

                }
            } else if (number == 4) {
                com_tab.setText(getResources().getString(R.string.computer_talets) + ">");
                com_tab.setTag("com_tab");
                com_tab.setTag(R.string.tag, "abcat0500000");
                for (int x = 0; x < 25; x++) {
                    inLay2.addView(getView(x, prods, this.id));

                }
            } else if (number == 1) {
                movies_music.setText(getResources().getString(R.string.movies_music) + ">");
                movies_music.setTag("movies_music");
                movies_music.setTag(R.string.tag, "abcat0600000");
                for (int x = 0; x < 25; x++) {
                    inLay3.addView(getView(x, prods, this.id));
                }
            } else if (number == 2) {
                videoGames.setText(getResources().getString(R.string.video_games) + ">");
                videoGames.setTag("videoGames");
                videoGames.setTag(R.string.tag, "abcat0700000");
                for (int x = 0; x < 25; x++) {
                    inLay4.addView(getView(x, prods, this.id));

                }
            } else if (number == 3) {
                mobiles.setText(getResources().getString(R.string.mobiles) + ">");
                mobiles.setTag("mobiles");
                mobiles.setTag(R.string.tag, "abcat0800000");
                for (int x = 0; x < 25; x++) {
                    inLay5.addView(getView(x, prods, this.id));
                }
            }


        }
    }

    private View getView(int x, ArrayList<Products> prods, String id) {
        View rootView = inflater.inflate(R.layout.groups, null);
        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        image.setTag(25 + x);
        image.setTag(R.string.tag, id);
        if (!prods.get(x).largeImage.equals("null")) {
            Picasso.with(getActivity().getApplicationContext())
                    .load(prods.get(x).largeImage)
                    .into(image);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(prods.get(x).mediumImage)
                    .into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(view);
            }
        });

        return rootView;
    }

    public void showInfo(View v) {

        Intent intent = new Intent(v.getContext(), SingleProductInfos.class);
        intent.putExtra("page", 2);
        intent.putExtra("position", Integer.parseInt("" + v.getTag()));
        intent.putExtra("url", url1 + v.getTag(R.string.tag) + url2);
        intent.putExtra("url3", url3);
        intent.putExtra("sorted", "false");

        startActivity(intent);
    }


    public void jumpToDetails(View v) {
        String tag = (String) v.getTag();
        Intent intent = new Intent(v.getContext(), AllProducts.class);

        if (tag.equals("tv_theater")) {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.tv_theater));
        } else if (tag.equals("com_tab")) {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.computer_talets));
        } else if (tag.equals("movies_music")) {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.movies_music));
        } else if (tag.equals("videoGames")) {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.video_games));
        } else {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.mobiles));
        }
        intent.putExtra("url2", url3);
        intent.putExtra("page", 2);
        intent.putExtra("decalage", 25);
        startActivity(intent);
    }




    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}


















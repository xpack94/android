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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    LinearLayoutManager layoutManager5,layoutManager4,layoutManager3,layoutManager2,layoutManager1;
    RecyclerView myList5,myList4,myList3,myList2,myList1;
    yourAdapter adapt5,adapt4,adapt3,adapt2,adapt1;
    ArrayList<Products> produits1 = new ArrayList<Products>();
    ArrayList<Products> produits2 = new ArrayList<Products>();
    ArrayList<Products> produits3 = new ArrayList<Products>();
    ArrayList<Products> produits4 = new ArrayList<Products>();
    ArrayList<Products> produits5 = new ArrayList<Products>();
    ArrayList<Products> produits6 = new ArrayList<Products>();
    ArrayList<Products> produits7 = new ArrayList<Products>();
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


                        //Thread.sleep(7000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                load();

                            }
                        });





                }
            }.start();



        }else{
            load();
        }



        return v;
    }




        //la methode load permet de charger les données dans l'activité principale

    public void load() {
        layoutManager5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        myList1 = (RecyclerView) v.findViewById(R.id.my_recycler_view1);
        myList1.setLayoutManager(layoutManager1);
        myList2 = (RecyclerView) v.findViewById(R.id.my_recycler_view2);
        myList2.setLayoutManager(layoutManager2);
        myList3 = (RecyclerView) v.findViewById(R.id.my_recycler_view3);
        myList3.setLayoutManager(layoutManager3);
        myList4 = (RecyclerView) v.findViewById(R.id.my_recycler_view4);
        myList4.setLayoutManager(layoutManager4);
        myList5 = (RecyclerView) v.findViewById(R.id.my_recycler_view5);
        myList5.setLayoutManager(layoutManager5);
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

        while (!haveNetworkConnection()){

        }



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
                if(id.equals("")){
                    Parser.getProducts(this.produits, url1  + page + url2);
                }else{
                    Parser.getProducts(this.produits, url1 + id + url2 + page + url3);
                }



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
            if(number==5){
                TextView t=(TextView) v.findViewById(R.id.tv_theater);
                tv_theater.setText(getResources().getString(R.string.tv_theater));
                tv_theater.setTag(R.string.tag, "abcat0100000");
                tv_theater.setTag("tv_theater");
                adapt5=new yourAdapter(produits,"abcat0100000");
                myList5.setAdapter(adapt5);

            }else  if (number == 4) {
                com_tab.setText(getResources().getString(R.string.computer_talets) + ">");
                com_tab.setTag("com_tab");
                com_tab.setTag(R.string.tag, "abcat0500000");
                adapt4=new yourAdapter(produits,"abcat0500000");
                myList4.setAdapter(adapt4);

            } else if (number == 1) {
                movies_music.setText(getResources().getString(R.string.movies_music) + ">");
                movies_music.setTag("movies_music");
                movies_music.setTag(R.string.tag, "abcat0600000");
                adapt1=new yourAdapter(produits,"abcat0600000");
                myList1.setAdapter(adapt1);
            } else if (number == 2) {
                videoGames.setText(getResources().getString(R.string.video_games) + ">");
                videoGames.setTag("videoGames");
                videoGames.setTag(R.string.tag, "abcat0700000");
                adapt2=new yourAdapter(produits,"abcat0700000");
                myList2.setAdapter(adapt2);
            } else {
                mobiles.setText(getResources().getString(R.string.mobiles) + ">");
                mobiles.setTag("mobiles");
                mobiles.setTag(R.string.tag, "abcat0800000");
                adapt3=new yourAdapter(produits,"abcat0800000");
                myList3.setAdapter(adapt3);
            }




        }
    }

//    private View getView(int x, ArrayList<Products> prods, String id) {
//        View rootView = inflater.inflate(R.layout.groups, null);
//        ImageView image = (ImageView) rootView.findViewById(R.id.image);
//        image.setTag(25 + x);
//        image.setTag(R.string.tag, id);
//        if (!prods.get(x).largeImage.equals("null")) {
//            Picasso.with(getActivity().getApplicationContext())
//                    .load(prods.get(x).largeImage)
//                    .into(image);
//        } else {
//            Picasso.with(getActivity().getApplicationContext())
//                    .load(prods.get(x).mediumImage)
//                    .into(image);
//        }
//
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showInfo(view);
//            }
//        });
//
//        return rootView;
//    }

    public void showInfo(View v) {

        Intent intent = new Intent(v.getContext(), SingleProductInfos.class);
        intent.putExtra("page", 2);
        intent.putExtra("position", Integer.parseInt("" + v.getTag()));
        intent.putExtra("url", url1 + v.getTag(R.string.tag) + url2);
        intent.putExtra("url3", url3);
        intent.putExtra("sorted", "false");
        intent.putExtra("type","");

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
        } else if(tag.equals("mobiles")) {
            intent.putExtra("url1", url1 + v.getTag(R.string.tag) + url2);
            intent.putExtra("title", "" + getResources().getString(R.string.mobiles));
        }else{
            String url="https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page=";
            String urll="&apiKey=tghcgc6qnf72tat8a5kbja9r";
            intent.putExtra("url1", url);
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

//
//    private class customAdapter extends BaseAdapter {
//
//
//        ArrayList<Products> produits;
//
//        public customAdapter(ArrayList<Products> produits) {
//            super();
//            this.produits = produits;
//        }
//
//        @Override
//        public int getCount() {
//            return produits.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return produits.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//            View row = inflater.inflate(R.layout.groups, parent, false);
//
//            ImageView image = (ImageView) row.findViewById(R.id.image);
//
//
//            if(produits.get(position).mediumImage!=null){
//                Log.e("t", "getView: "+produits.get(position).mediumImage );
//                Picasso.with(getActivity().getApplicationContext())
//                        .load(produits.get(position).mediumImage)
//                        .into(image);
//            }
//
//            return row;
//        }
//    }



    public  class yourAdapter extends RecyclerView.Adapter<yourAdapter.SimpleViewHolder> implements View.OnClickListener  {


        ArrayList<Products> prods;
        String id;
        public yourAdapter(ArrayList<Products> produit,String id) {
            prods=produit;
            this.id=id;

        }

        @Override
        public yourAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups, parent, false);

            return new yourAdapter.SimpleViewHolder(view);
        }



        @Override
        public void onBindViewHolder(final yourAdapter.SimpleViewHolder holder, final int position) {




            if(prods.get(position).mediumImage!=null){
                Picasso.with(getActivity().getApplicationContext())
                        .load(prods.get(position).mediumImage)
                        .into(holder.image);
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.image.setTag(25+position);
                    holder.image.setTag(R.string.tag, id);
                    showInfo(view);
                }
            });




        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return 25;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public void onClick(View view) {
            int itemPosition = myList5.getChildPosition(view);

        }


        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            ImageView image;


            public  SimpleViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);


            }
        }



    }





        }


















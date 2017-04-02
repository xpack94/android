package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {


    ListView liste;
    LayoutInflater inflater;
    ProgressDialog progress;
    LinearLayout inLay,inLay2,inLay3,inLay4,inLay5;
    int page=2;
    String [] categories= new String[2];
    int line=1;
    String url1="https://api.bestbuy.com/v1/products(categoryPath.id=";
    String url2="*)?format=json&show=all&pageSize=25&page=";
    String url3="&apiKey=tghcgc6qnf72tat8a5kbja9r";
    int cat=0;
    ArrayList<Products> produits1=new ArrayList<Products>();
    ArrayList<Products> produits2=new ArrayList<Products>();
    ArrayList<Products> produits3=new ArrayList<Products>();
    ArrayList<Products> produits4=new ArrayList<Products>();
    ArrayList<Products> produits5=new ArrayList<Products>();
    TextView tv_theater, com_tab,mobiles,videoGames, movies_music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=(View ) navigationView.getHeaderView(0);
        ImageView v= (ImageView) header.findViewById(R.id.imageView);
        //tester si le telephone est connecté a internet
        //afficher une boite de dialogue sinon
        if(!haveNetworkConnection()){
            NoInternet ni =new NoInternet();
            ni.show(getFragmentManager(),"dialog");
            //creer un thread qui attend que le button ok soit appuyer pour faire appele a la methode load
            //afin de charger les données
            new Thread(){
                public void run(){
                    while (!NoInternet.clicked && !haveNetworkConnection()){

                    }
                    try {
                        Thread.sleep(6000);
                        runOnUiThread(new Runnable() {
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



        }








    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment f=null;
        if (id == R.id.Computer_Tablets) {
            f=new Computers_Tablets();
        } else if (id == R.id.all_products) {
            Intent intent=new Intent(getApplicationContext(),AllProducts.class);
            startActivity(intent);



        } else if (id == R.id.tv_theater) {

        } else if (id == R.id.audio) {

        } else if (id == R.id.movies_music) {

        } else if (id == R.id.video_games) {

        }else if (id==R.id.Mobies){

        }
        else if (id==R.id.Cameras){

        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, f).commit();


//        android.support.v4.app.FragmentTransaction fragmentTransaction =
//                getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, f);
//        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    //la methode load permet de charger les données dans l'activité principale
    public void load(){



            inflater=getLayoutInflater();
            inLay=(LinearLayout) findViewById(R.id.innerLay);
            inLay2=(LinearLayout) findViewById(R.id.innerLay2);
            inLay3=(LinearLayout) findViewById(R.id.innerLay3);
            inLay4=(LinearLayout) findViewById(R.id.innerLay4);
            inLay5=(LinearLayout) findViewById(R.id.innerLay5);
            tv_theater=(TextView) findViewById(R.id.tv_theater);
            com_tab=(TextView) findViewById(R.id.Computer_Tablets);
            mobiles=(TextView) findViewById(R.id.mobiles);
            videoGames=(TextView) findViewById(R.id.video_games);
            movies_music=(TextView) findViewById(R.id.Movies_Music);

            tv_theater.setTextColor(Color.parseColor("#FFFFFF"));
            com_tab.setTextColor(Color.parseColor("#FFFFFF"));
            videoGames.setTextColor(Color.parseColor("#FFFFFF"));
            movies_music.setTextColor(Color.parseColor("#FFFFFF"));
            mobiles.setTextColor(Color.parseColor("#FFFFFF"));
            progress=new ProgressDialog(this);


            Fetcher l = new Fetcher(1,url1,url2,"abcat0100000",produits1);
            Fetcher k = new Fetcher(2,url1,url2,"abcat0500000",produits2);
            //movies and music
            Fetcher f = new Fetcher(3,url1,url2,"abcat0600000",produits3);
            //video games
            Fetcher j = new Fetcher(4,url1,url2,"abcat0700000",produits4);
            //mobiles
            Fetcher w = new Fetcher(5,url1,url2,"abcat0800000",produits5);

            l.execute();
            k.execute();
            f.execute();
            j.execute();
            w.execute();


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


                Parser.getProducts(this.produits, url1 + id +url2+page+ url3);


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
            if (number == 1) {
                tv_theater.setText(R.string.tv_theater);
                for (int x = 0; x < 25; x++) {
                    inLay.addView(getView(x, prods,this.id));

                }
            } else if (number == 2) {
                com_tab.setText(R.string.computer_talets);
                for (int x = 0; x < 25; x++) {
                    inLay2.addView(getView(x, prods,this.id));

                }
            } else if (number == 3) {
                movies_music.setText(R.string.movies_music);
                for (int x = 0; x < 25; x++) {
                    inLay3.addView(getView(x, prods,this.id));
                }
            } else if (number == 4) {
                videoGames.setText(R.string.video_games);
                for (int x = 0; x < 25; x++) {
                    inLay4.addView(getView(x, prods,this.id));

                }
            } else if (number == 5) {
                mobiles.setText(R.string.mobiles);
                for (int x = 0; x < 25; x++) {
                    inLay5.addView(getView(x, prods,this.id));
                }
            }
           // showInfo();

        }
    }
    private View getView(int x,ArrayList<Products> prods,String id) {
        View rootView = inflater.inflate( R.layout.groups,null);
        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        image.setTag(25+x);
        image.setTag(R.string.tag,id);
        Picasso.with(getApplicationContext())
                .load(prods.get(x).mediumImage)
                .into(image);


        return rootView;
    }

    public void showInfo(View v){

        Intent intent = new Intent(MainActivity.this,SingleProductInfos.class);
        intent.putExtra("page", 2);
        intent.putExtra("position", Integer.parseInt(""+v.getTag()));
        intent.putExtra("url",url1+v.getTag(R.string.tag)+url2);
        intent.putExtra("url3",url3);
        intent.putExtra("offset",2);

        startActivity(intent);
       // Log.e("t", ""+url1+v.getTag(R.string.tag)+url2 );
    }




    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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















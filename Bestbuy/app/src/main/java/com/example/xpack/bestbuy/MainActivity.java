package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
    String [] categories= new String[2];
    int line=1;
    String url1="https://api.bestbuy.com/v1/products(categoryPath.id=";
    String url2="*)?format=json&show=all&pageSize=25&page=2&apiKey=tghcgc6qnf72tat8a5kbja9r";
    int cat=0;
    ArrayList<Products> produits1=new ArrayList<Products>();
    ArrayList<Products> produits2=new ArrayList<Products>();
    ArrayList<Products> produits3=new ArrayList<Products>();
    ArrayList<Products> produits4=new ArrayList<Products>();
    ArrayList<Products> produits5=new ArrayList<Products>();

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
      //  v.setImageResource(R.drawable.icon);
//        TextView header_text=(TextView) header.findViewById(R.id.head);
//        header_text.setText("BestBuy");


        inflater=getLayoutInflater();
        inLay=(LinearLayout) findViewById(R.id.innerLay);
        inLay2=(LinearLayout) findViewById(R.id.innerLay2);
        inLay3=(LinearLayout) findViewById(R.id.innerLay3);
        inLay4=(LinearLayout) findViewById(R.id.innerLay4);
        inLay5=(LinearLayout) findViewById(R.id.innerLay5);
        TextView tv_theater=(TextView) findViewById(R.id.tv_theater);
        TextView com_tab=(TextView) findViewById(R.id.Computer_Tablets);
        TextView mobiles=(TextView) findViewById(R.id.mobiles);
        TextView videoGames=(TextView) findViewById(R.id.video_games);
        TextView movies_music=(TextView) findViewById(R.id.Movies_Music);

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

//            line++;
            k.execute();
            f.execute();
            j.execute();
            w.execute();





            //appelle a la methode getView pour remplir le layout avec des images








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
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.setTitle("loading products");
            progress.show();
        }


        @Override
        protected ArrayList<Products> doInBackground(Object... params) {


            try {


                Parser.getProducts(this.produits, url1 + id + url2);


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

                for (int x = 0; x < 25; x++) {
                    inLay.addView(getView(x, prods));

                }
            } else if (number == 2) {
                for (int x = 0; x < 25; x++) {
                    inLay2.addView(getView(x, prods));

                }
            } else if (number == 3) {
                for (int x = 0; x < 25; x++) {
                    inLay3.addView(getView(x, prods));
                }
            } else if (number == 4) {
                for (int x = 0; x < 25; x++) {
                    inLay4.addView(getView(x, prods));

                }
            } else if (number == 5) {
                for (int x = 0; x < 25; x++) {
                    inLay5.addView(getView(x, prods));
                }
            }

        }
    }
    private View getView(int x,ArrayList<Products> prods) {

        View rootView = inflater.inflate( R.layout.groups,null);
        ImageView image = (ImageView) rootView.findViewById(R.id.image);
        Picasso.with(getApplicationContext())
                .load(prods.get(x).mediumImage)
                .into(image);
        return rootView;
    }





    }















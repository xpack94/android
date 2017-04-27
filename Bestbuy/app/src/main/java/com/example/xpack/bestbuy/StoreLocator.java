package com.example.xpack.bestbuy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xpack on 23/04/17.
 */

public class StoreLocator extends Activity implements Serializable, NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    SearchView search;
    ListView storesList;
    ProgressDialog progress;
    Button btnLoadMore;
    ArrayList<Stores> stores = new ArrayList<Stores>();
    int page = 1, pos = 1;
    Boolean visible = true, exist = false;
    ImageView logo, share;
    String url1 = "https://api.bestbuy.com/v1/stores?format=json&show=all&pageSize=25&page=";
    String url2 = "&apiKey=tghcgc6qnf72tat8a5kbja9r";
    StoresAdapter mAdapter;
    double lat;
    double lng;
    boolean fromGps=false;
    SearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.store_locator);

        toolbar = (Toolbar) findViewById(R.id.tool);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = (View) navigationView.getHeaderView(0);
        ImageView v = (ImageView) header.findViewById(R.id.imageView);


        storesList = (ListView) findViewById(R.id.storesList);
        progress = new ProgressDialog(this);
        btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
        btnLoadMore.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                pos++;


                Fetcher l = new Fetcher();
                l.execute();
                //si visible=false alors le button affichera qu'il y'a plus aucun autre produit a afficher
                if (!visible) {
                    btnLoadMore.setText(getResources().getString(R.string.footer_store_text));
                    btnLoadMore.setEnabled(false);
                }
            }
        });

        logo = (ImageView) findViewById(R.id.logo);
        share = (ImageView) findViewById(R.id.share);

        share.setVisibility(View.GONE);
        Picasso.with(getApplicationContext())
                .load("http://www.userlogos.org/files/logos/mafi0z/BestBuy.png")
                .into(logo);

        search = (SearchView) findViewById(R.id.searchView);
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setVisibility(View.GONE);


            }
        });
        //reafficher le logo quand on quite la recherche
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logo.setVisibility(View.VISIBLE);

                return false;
            }
        });
        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //si l'utilisateur tape un nom de produit a rechercher
                //appeler l'activité AllProducts qui a son tours s'occupe de chercher
                //le nom du produit tapé par l'utilisateur
                Intent intent=new Intent(StoreLocator.this,AllProducts.class);
                String ur="https://api.bestbuy.com/v1/products(search=";
                String word="";
                Boolean word_found=false;
                int i=0;
                while(i<query.length()){
                    Log.e("t", "the inde is  "+i );
                    while((i<query.length()) && (query.charAt(i)!=' ')){

                        word+=query.charAt(i);
                        word_found=true;
                        i++;
                        Log.e("t", "index:"+i+" word = "+word);
                        // Log.e("t", "the word is  "+word );

                    }

                    if(word_found){
                        word+=" ";
                        word_found=false;
                    }

                    i++;
                }
                ur+=word.replaceAll(" ","&search=");
                ur=ur.substring(0,ur.length()-8);
                ur+=")?format=json&show=all&pageSize=25&page=";

                //  intent.putExtra("url1","https://api.bestbuy.com/v1/products(name="+query+"*%7Csearch="+query+"*)?format=json&show=all&pageSize=25&page=");
                intent.putExtra("url1",ur);
                intent.putExtra("url2","&apiKey=tghcgc6qnf72tat8a5kbja9r");
                intent.putExtra("page",1);
                intent.putExtra("decalage",0);
                intent.putExtra("title",getResources().getString(R.string.search_results));
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        Fetcher f= new Fetcher();

        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
             lat= tracker.getLatitude();
             lng= tracker.getLongitude();
            fromGps=true;
            f.execute();

        }


        if(!fromGps){
            f.execute();

        }


    }


    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Stores>> {


        public void onPreExecute() {


            progress.setCancelable(false);
            progress.setTitle(""+getResources().getString(R.string.loadingStores));
            progress.show();
        }

        @Override
        protected ArrayList<Stores> doInBackground(Object... params) {


            try {

                int size = stores.size();
                if(fromGps){
                    String ur="https://api.bestbuy.com/v1/stores(area("+lat+","+lng+",100))?format=json&show=all&pageSize=25&page=";
                    StoresParser.getStores(stores, ur + page + url2);
                    Log.e("e", ": "+ur );
                }else{
                    StoresParser.getStores(stores, url1 + page + url2);
                }



                //verifier si on est rendu au dernier produit
                //si oui le boutton load More deviendra invisible
                if (size == stores.size()) {
                    visible = false;
                }



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
            if (pos == 1) {
                mAdapter =new StoresAdapter(stores);
                storesList.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();


            }




            //verifier que le tableau produits n'est pas vide
            //si le tableau est vide afficher que aucun produit n'a etait trouvé

            if(stores.size()!=0){
                if(exist==false){
                    storesList.addFooterView(btnLoadMore);
                    exist=true;
                }
            }else{
                storesList.setVisibility(View.GONE);

            }

            storesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(StoreLocator.this,StoreDetails.class);
                    intent.putExtra("page",page);
                    intent.putExtra("position",i);

                    startActivity(intent);
                }
            });


        }


    }

    class StoresAdapter extends BaseAdapter{

        ArrayList<Stores> stores;

        public StoresAdapter(ArrayList<Stores> stores) {
            super();
            this.stores = stores;
        }

        @Override
        public int getCount() {
            return stores.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.stores_list_view_layout, parent, false);

                TextView name = (TextView) convertView.findViewById(R.id.storeName);
                TextView address = (TextView) convertView.findViewById(R.id.address);
                TextView phoneNumber=(TextView) convertView.findViewById(R.id.phone);



            name.setText(stores.get(position).longName);
            address.setText(stores.get(position).address1+""+stores.get(position).addres2) ;
            phoneNumber.setText(stores.get(position).phone);

            return convertView;
        }

    }







    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), AllProducts.class);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment f = null;
        if (id == R.id.HOME) {

            Intent i=new Intent(StoreLocator.this,Main.class);
            startActivity(i);
            return true;
        } else if (id == R.id.all_products) {
            intent.putExtra("url1","https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page=");
            intent.putExtra("title",""+getResources().getString(R.string.all_products));

        } else if (id == R.id.itemsOnSale) {

            intent.putExtra("url1", "https://api.bestbuy.com/v1/products(onSale=true)?format=json&show=all&pageSize=25&page=");
            intent.putExtra("title", "" + getResources().getString(R.string.onSale));
        } else if (id == R.id.wishlist) {
            Intent i=new Intent(StoreLocator.this,WishList.class);
            startActivity(i);
            return true;
        } else if (id == R.id.store) {

            drawer.closeDrawers();
            return true;
        } else if (id == R.id.settings) {
            Intent inte=new Intent(StoreLocator.this,Settings.class);
            startActivity(inte);
            return true;
        }

        intent.putExtra("url2", "&apiKey=tghcgc6qnf72tat8a5kbja9r");
        intent.putExtra("page", 1);
        intent.putExtra("decalage", 0);
        startActivity(intent);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}


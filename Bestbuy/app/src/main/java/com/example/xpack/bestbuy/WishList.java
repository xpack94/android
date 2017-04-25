package com.example.xpack.bestbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by xpack on 11/04/17.
 */

public class WishList extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    SearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_wish_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = (View) navigationView.getHeaderView(0);
        ImageView v = (ImageView) header.findViewById(R.id.imageView);


        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);



        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if (position == 0)
                    return new FavoritesActivity();
                if (position == 1) {
                    return new MyLists();
                }

                return new MyLists();
            }

            @Override
            public int getCount() {
                return 2;
            }

            private String[] titles = new String[]{
                    "all products",
                    "My Lists"
            };

            @Override
            public CharSequence getPageTitle(int position) {

                return titles[position];
            }
        });
        tabs.setupWithViewPager(pager);



        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //si l'utilisateur tape un nom de produit a rechercher
                //appeler l'activité AllProducts qui a son tours s'occupe de chercher
                //le nom du produit tapé par l'utilisateur
                Intent intent=new Intent(WishList.this,AllProducts.class);
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
                ur+=word.replaceAll(" ","&search=")+"*)?format=json&show=all&pageSize=25&page=";


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


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), AllProducts.class);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment f = null;
        if (id == R.id.HOME) {

            Intent i = new Intent(WishList.this, Main.class);
            startActivity(i);
            return true;
        } else if (id == R.id.all_products) {

            intent.putExtra("url1","https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page=");
            intent.putExtra("title",""+getResources().getString(R.string.all_products));
            intent.putExtra("url2", "&apiKey=tghcgc6qnf72tat8a5kbja9r");
            intent.putExtra("page", 1);
            intent.putExtra("decalage", 0);
            startActivityForResult(intent, 1);
            onActivityResult(1, Activity.RESULT_OK, intent);
            return true;


        } else if (id == R.id.itemsOnSale) {

            intent.putExtra("url1", "https://api.bestbuy.com/v1/products(onSale=true)?format=json&show=all&pageSize=25&page=");
            intent.putExtra("title", "" + getResources().getString(R.string.onSale));
        } else if (id == R.id.wishlist) {
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.store) {
            Intent i =new Intent(WishList.this,StoreLocator.class);
            startActivity(i);
            return true;

        } else if (id == R.id.settings) {
            return true;
        }

        intent.putExtra("url2", "&apiKey=tghcgc6qnf72tat8a5kbja9r");
        intent.putExtra("page", 1);
        intent.putExtra("decalage", 0);
        startActivity(intent);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Invalidate list if wishlist is changed
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                Log.e("e", "onActivityResult:1 " );
                Boolean changed = data.getExtras().getBoolean("changedFavorite");

                if (changed){
                    recreate();
                }
            }
        }else {
            if(resultCode == Activity.RESULT_OK) {
                Log.e("e", "onActivityResult:3 " );
                recreate();

            }
        }
    }

    public void onBackPressed(){
        //to update list if changed favorite (wishlist)
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}

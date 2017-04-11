package com.example.xpack.bestbuy;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by xpack on 11/04/17.
 */

public class WishList extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_wish_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//
//
//        View header=(View ) navigationView.getHeaderView(0);
//        ImageView v= (ImageView) header.findViewById(R.id.imageView);
//


        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);



        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if(position == 0)
                    return new FavoritesActivity();
                if(position==1){
                    return new BestSellings();
                }

                return new BestSellings();
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


    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}

package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class SingleProductInfos extends AppCompatActivity {

    private String url,url3;
    private int page;
    private ViewPager pager;
    private ProgressDialog progress;
    private int startPosition;
    ArrayList<Products> produits = new ArrayList<Products>();
    pagerAdaper mAdapter ;
    int pos=0,offset;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_product_infos);
        Bundle args = getIntent().getExtras();
        mAdapter= new pagerAdaper(getSupportFragmentManager());
        progress = new ProgressDialog(this);
        page = args.getInt("page");
        url= args.getString("url");
        url3= args.getString("url3");
        offset=args.getInt("offset");

        startPosition = args.getInt("position");
        // produits= (ArrayList<Products>) args.getSerializable("produits");
        pager = (ViewPager) findViewById(R.id.pager);
        Fetcher fetcher = new Fetcher(url,url3);
        fetcher.execute();



        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



                if (position % 24 == 0) {

                }
            }

            @Override
            public void onPageSelected(int position) {
                pos=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(pos%24==0 && state==1){
                    if (pos+offset==produits.size()){

                        produits = new ArrayList<Products>();
                        mAdapter.notifyDataSetChanged();
                        page++;
                        startPosition = pos+1;
                        Fetcher fetcher = new Fetcher(url,url3);
                        fetcher.execute();
                        pos++;
                        offset++;
                    }

               }

                }


        });


    }

    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Products>> {
        String url,url3;
        public Fetcher(String url,String url3){

            this.url=url;
            this.url3=url3;
        }

        public void onPreExecute() {

            progress.setCancelable(false);
            progress.setTitle("loading products");
            progress.show();
        }

        @Override
        protected ArrayList<Products> doInBackground(Object... params) {


            try {
                for (int i = 1; i <= page; i++) {

                    Parser.getProducts(produits, this.url+i +this.url3);
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

            pager.setAdapter(mAdapter);

            pager.setCurrentItem(startPosition);



        }





    }

    public class pagerAdaper extends FragmentPagerAdapter {


        public pagerAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Products prod = produits.get(position);
            SingleProductFragment fragment = new SingleProductFragment();
            Bundle args = new Bundle();
            args.putString("name", prod.name);
            args.putString("largeImage", prod.largeImage);
            args.putString("salePrice", prod.salePrice);
            args.putString("salesEnd", prod.salesEnd);
            args.putString("ratingCount", prod.ratingCount);
            args.putString("ratings", prod.customerReview);
            args.putString("isAvailable", prod.isAvailable);
            args.putString("longDescription", prod.longDescription);
            args.putString("addToCartUrl", prod.addToCartUrl);

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return produits.size();
        }

    }
}

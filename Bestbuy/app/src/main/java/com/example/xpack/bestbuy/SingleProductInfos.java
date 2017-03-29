package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class SingleProductInfos extends AppCompatActivity {

    private String url;
    private int page;
    private ViewPager pager;
    private ProgressDialog progress;
    private int startPosition;
    ArrayList<Products> produits=new ArrayList<Products>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_product_infos);
        Bundle args=getIntent().getExtras();
        progress=new ProgressDialog(this);
        page=args.getInt("page");
        startPosition =args.getInt("position");
       // produits= (ArrayList<Products>) args.getSerializable("produits");
        pager= (ViewPager) findViewById(R.id.pager);
        Fetcher fetcher = new Fetcher();
        fetcher.execute();


    }

    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Products>> {



        public void onPreExecute() {

            progress.setCancelable(false);
            progress.setTitle("loading products");
            progress.show();
        }

        @Override
        protected ArrayList<Products> doInBackground(Object... params) {



            try {
                for (int i =1;i<=page;i++){
                    Parser.getProducts(produits,"https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page="+i+"&apiKey=tghcgc6qnf72tat8a5kbja9r");
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

            pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public int getCount() {
                    return produits.size() ;
                }

                @Override
                public Fragment getItem(int position) {

                    Products prod=produits.get(position);
                    SingleProductFragment fragment=new SingleProductFragment();
                    Bundle args=new Bundle();
                    args.putString("name",prod.name);
                    args.putString("largeImage",prod.largeImage);
                    args.putString("salePrice",prod.salePrice);
                    args.putString("salesEnd",prod.salesEnd);
                    args.putString("ratingCount",prod.ratingCount);
                    args.putString("ratings",prod.customerReview);
                    args.putString("isAvailable",prod.isAvailable);
                    args.putString("longDescription",prod.longDescription);

                    fragment.setArguments(args);

                    return fragment;
                }
            });
            pager.setCurrentItem(startPosition);
        }




    }
}

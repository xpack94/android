package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by xpack on 24/03/17.
 */

public class AllProducts extends AppCompatActivity {


    ListView list;
    int page=1;
    ProgressDialog pDialog;
    itemsAdapter mAdapter;
    Products [] produits;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        Intent intent=getIntent();
        list= (ListView) findViewById(R.id.list);
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
       // list.setOnScrollListener(new EndlessScrollListener());
        btnLoadMore.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                    page++;

                Fetch l= new Fetch();
                l.execute();


            }
        });
        list.addFooterView(btnLoadMore);

        Fetcher l= new Fetcher();
        l.execute();









    }



    public class Fetcher extends AsyncTask<Object, Object, Products[]> {





        @Override
        protected Products[] doInBackground(Object... params) {

                produits=new Products[0];

            try {
                produits = Parser.getProducts("https://api.bestbuy.com/v1/products?format=json&show=mediumImage,name,salePrice&pageSize=25&page="+(page)+"&apiKey=tghcgc6qnf72tat8a5kbja9r");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return produits;
        }

        @Override
        protected void onPostExecute(final Products[] produits) {
            mAdapter=new itemsAdapter(produits);
            mAdapter.notifyDataSetChanged();
            list.setAdapter(mAdapter);

    }



}
    class  itemsAdapter extends BaseAdapter{

        Products [] produits;
        public itemsAdapter( Products[] produits){
            super();
            this.produits=produits;
        }

        @Override
        public int getCount() {
            return produits.length;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.products, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView sku = (TextView) convertView.findViewById(R.id.salePrice);
            ImageView image = (ImageView) convertView.findViewById(R.id.image);


                name.setText(produits[position].name);
                sku.setText(produits[position].salePrice);

                Picasso.with(getApplicationContext())
                        .load(produits[position].url)
                        .into(image);





            return convertView;
        }


    }







    public class Fetch extends AsyncTask<Object, Object, Products[]> {





        @Override
        protected Products[] doInBackground(Object... params) {




            try {
                produits = Parser.getProducts("https://api.bestbuy.com/v1/products?format=json&show=mediumImage,name,salePrice&pageSize=25&page="+(page)+"&apiKey=tghcgc6qnf72tat8a5kbja9r");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return produits;
        }

        @Override
        protected void onPostExecute(final Products[] produits) {

//            int currentPosition = list.getFirstVisiblePosition();



           // list.setSelectionFromTop(currentPosition + 1, 0);
            list.setAdapter(mAdapter);



        }

    }




    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {


            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
//                new LoadGigsTask().execute(currentPage + 1);
                Toast.makeText(getApplicationContext(),""+currentPage,Toast.LENGTH_LONG).show();

                 Fetcher l = new Fetcher();
                l.execute(currentPage+1);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }























}

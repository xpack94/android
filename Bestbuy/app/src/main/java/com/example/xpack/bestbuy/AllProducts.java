package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class AllProducts extends AppCompatActivity implements Serializable {


    ListView list;
    int page=1;
    ProgressDialog pDialog;
    itemsAdapter mAdapter;
    int pos=1;

    ArrayList<Products> produits=new ArrayList<Products>();
    ProgressDialog progress ;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        Intent intent=getIntent();
        progress=new ProgressDialog(this);
        list= (ListView) findViewById(R.id.list);
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
       // list.setOnScrollListener(new EndlessScrollListener());
        btnLoadMore.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                    page++;
                    pos++;
                Fetcher l = new Fetcher();
                l.execute();
            }
        });
        list.addFooterView(btnLoadMore);

        Fetcher l= new Fetcher();
        l.execute();









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


                Parser.getProducts(produits,"https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page="+page+"&apiKey=tghcgc6qnf72tat8a5kbja9r");
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
            if (pos==1){
                mAdapter=new itemsAdapter(produits);
                list.setAdapter(mAdapter);
            }else{
                mAdapter.notifyDataSetChanged();


            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AllProducts.this, SingleProductInfos.class);

                    intent.putExtra("page", page);
                    intent.putExtra("position", position);
                   // intent.putExtra("produits",produits);

                    startActivity(intent);
                }
            });




        }




    }
    class  itemsAdapter extends BaseAdapter{


        ArrayList<Products> produits;
        public itemsAdapter(ArrayList<Products> produits){
            super();
            this.produits=produits;
        }

        @Override
        public int getCount() {
            return produits.size();
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
            //TextView customerReview =(TextView) convertView.findViewById(R.id.customerReview);
            RatingBar rat= (RatingBar) convertView.findViewById(R.id.ratings);



                name.setText(produits.get(position).name);
                sku.setText(produits.get(position).salePrice);
                if (produits.get(position).customerReview=="null"){
                    rat.setRating(Float.parseFloat("0"));

            }else{

                    rat.setRating(Float.parseFloat(produits.get(position).customerReview));
                }



                Picasso.with(getApplicationContext())
                        .load(produits.get(position).url)
                        .into(image);





            return convertView;
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

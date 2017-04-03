package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class AllProducts extends AppCompatActivity implements Serializable {


    ListView list;
    int page;
    ProgressDialog pDialog;
    itemsAdapter mAdapter;
    int pos=1,decalage;
    String url1,url2;
    String title;
    Button btnLoadMore;
    Boolean exist=false;

    ArrayList<Products> produits=new ArrayList<Products>();
    ProgressDialog progress ;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.test);

        Intent intent=getIntent();
        title=intent.getExtras().getString("title");

        url1=intent.getExtras().getString("url1");
        url2=intent.getExtras().getString("url2");
        page=intent.getExtras().getInt("page");
        decalage=intent.getExtras().getInt("decalage");
        setTitle(title);

        progress=new ProgressDialog(this);
        list= (ListView) findViewById(R.id.list);
        btnLoadMore = new Button(this);
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


                Parser.getProducts(produits,url1+page+url2);
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
                    intent.putExtra("position", decalage+position);
                    intent.putExtra("url",url1);
                    intent.putExtra("url3",url2);
                    //le offset est utiliser dans le viewpager pour savoir quand est-ce que on atteint la fin du viewpager et
                    //faire un load more


                   // intent.putExtra("produits",produits);

                    startActivity(intent);
                }
            });

            //verifier que le tableau produits n'est pas vide
            //si le tableau est vide afficher que aucun produit n'a etait trouvé
            if(produits.size()!=0){
                if(exist==false){
                    list.addFooterView(btnLoadMore);
                    exist=true;
                }
            }else{
                list.setVisibility(View.GONE);
                RelativeLayout r=(RelativeLayout) findViewById(R.id.layout_empty);
                r.setVisibility(View.VISIBLE);
            }



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
            ImageView pic=(ImageView) convertView.findViewById(R.id.AvailablOnline);
            TextView avOnline =(TextView) convertView.findViewById(R.id.AvailablOnlineText);



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

            //verifier si le produit et disponible en ligne
            if (produits.get(position).isAvailable.equals("true")){
                pic.setImageResource(R.drawable.available_24dp);
                avOnline.setText(R.string.available_online);
                avOnline.setTextColor(Color.parseColor("#2F4F4F"));
            }else{
                pic.setImageResource(R.drawable.ic_block_black_24dp);
                avOnline.setText(R.string.not_available);
                avOnline.setTextColor(Color.parseColor("#2F4F4F"));

            }






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

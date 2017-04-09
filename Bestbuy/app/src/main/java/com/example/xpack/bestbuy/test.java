package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class test extends AppCompatActivity implements Serializable {


    ListView list;
    int page;
    ProgressDialog pDialog;
    itemsAdapter mAdapter;
    int pos=1,decalage;
    String url1,url2;
    String title;
    Button btnLoadMore;
    Boolean exist=false;
    Toolbar toolbar;
    ArrayList<Products> produits=new ArrayList<Products>();
    ProgressDialog progress ;
    ImageView logo,share,togglerImage;
    SearchView search;
    Boolean visible=true;
    Button sortPrice,sortRatings;
    String sorted="true",type;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.lay);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView toggler=(ImageView) findViewById(R.id.toggler);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(true);



        toggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        toolbar= (Toolbar) findViewById(R.id.toolbar);
        logo=(ImageView) findViewById(R.id.logo);
        share=(ImageView) findViewById(R.id.share);
        search=(SearchView) findViewById(R.id.searchView);
        togglerImage=(ImageView) findViewById(R.id.toggler);
        share.setVisibility(View.GONE);
        // toolbar.setAlpha(Float.parseFloat("0.5"));
        Picasso.with(getApplicationContext())
                .load("http://www.userlogos.org/files/logos/mafi0z/BestBuy.png")
                .into(logo);


        RelativeLayout sortingButtons =(RelativeLayout) findViewById(R.id.sortingButtons);
        sortingButtons.setVisibility(View.GONE);
        sortPrice=(Button) findViewById(R.id.sortPrice);
        sortPrice.setVisibility(View.GONE);
        sortRatings=(Button) findViewById(R.id.sortRank);
        sortRatings.setVisibility(View.GONE);

        Intent intent=getIntent();

        title=intent.getExtras().getString("title");

        url1=intent.getExtras().getString("url1");
        url2=intent.getExtras().getString("url2");
        page=intent.getExtras().getInt("page");
        decalage=intent.getExtras().getInt("decalage");
        type=intent.getExtras().getString("type");
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
                //si visible=false alors le button affichera qu'il y'a plus aucun autre produit a afficher
                if(!visible){
                    btnLoadMore.setText(getResources().getString(R.string.footer_text));
                    btnLoadMore.setEnabled(false);
                }
            }
        });

        Fetcher l= new Fetcher();
        l.execute();


        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setVisibility(View.GONE);
                togglerImage.setVisibility(View.GONE);


            }
        });
        //reafficher le logo quand on quite la recherche
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logo.setVisibility(View.VISIBLE);
                togglerImage.setVisibility(View.VISIBLE);
                return false;
            }
        });



        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    //si l'utilisateur tape un nom de produit a rechercher
                    //appeler l'activité AllProducts qui a son tours s'occupe de chercher
                    //le nom du produit tapé par l'utilisateur
                    Intent intent=new Intent(test.this,AllProducts.class);
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
                    ur+=word.replaceAll(" ","&search=")+"*)?format=json&shwo=all&pageSize=25&page=";
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



    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Products>> {




        public void onPreExecute() {


            progress.setCancelable(false);
            progress.setTitle("loading products");
            progress.show();
        }

        @Override
        protected ArrayList<Products> doInBackground(Object... params) {



            try {

                int size=produits.size();
                Parser.getProducts(produits,url1+page+url2);

//
                MergeSort a=new MergeSort(produits,type);
                a.sortGivenArray();
                //verifier si on est rendu au dernier produit
                //si oui le boutton load More deviendra invisible
                if (size==produits.size()){
                    visible=false;
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
            if (pos==1){
                mAdapter=new itemsAdapter(produits);
                list.setAdapter(mAdapter);
            }else{
                mAdapter.notifyDataSetChanged();


            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(test.this, SingleProductInfos.class);

                    intent.putExtra("page", page);
                    intent.putExtra("position", decalage+position);
                    intent.putExtra("url",url1);
                    intent.putExtra("url3",url2);
                    intent.putExtra("type",type);
                    intent.putExtra("sorted",sorted);
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


}

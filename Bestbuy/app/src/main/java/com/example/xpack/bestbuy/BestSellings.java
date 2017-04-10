package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class BestSellings extends Fragment {

    View v;
    GridView grid;
    ProgressDialog progress;
    String url1="https://api.bestbuy.com/v1/products(bestSellingRank>100000)?format=json&show=all&pageSize=25&page=";
    String url2="&apiKey=tghcgc6qnf72tat8a5kbja9r";
    int page=1,pos=1;
    itemsAdapter mAdapter;
    int decalage=0,offset=0;
    String sorted="false",type="";
    Boolean scrolled=false;
    ArrayList<Products> produits=new ArrayList<Products>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.best_sellings, container, false);
        grid=(GridView) v.findViewById(R.id.gridView);
        progress=new ProgressDialog(getActivity());

        if(!haveNetworkConnection()){

            new Thread(){
                public void run(){
                    while (!NoInternet.clicked && !haveNetworkConnection()){

                    }


                    //Thread.sleep(7000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pos=1;
                            Fetcher f=new Fetcher();
                            f.execute();
                        }
                    });





                }
            }.start();



        }else{
            pos=1;
            Fetcher f=new Fetcher();
            f.execute();
        }



        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                scrolled=true;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                //detecter si on a scrollé jusqu'a la fin
                //si oui on charge 25 autres produits
                final int lastItem = i + i1;
                if(scrolled){
                    if(lastItem == i2){

                        Fetcher f = new Fetcher();
                        page++;
                        pos++;
                        f.execute();
                        scrolled=false;
                    }
                }

        }


    });
        return v;


    }



    public class Fetcher extends AsyncTask<Object, Object, ArrayList<Products>> {



        public void onPreExecute() {


            progress.setCancelable(false);
            progress.setTitle("loading products");
            progress.show();
        }

        @Override
        protected ArrayList<Products> doInBackground(Object... params) {

            while (!haveNetworkConnection()){

            }


            try {

                Parser.getProducts(produits,url1+page+url2);

                //verifier si on est rendu au dernier produit
                //si oui le boutton load More deviendra invisible

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
                if(pos==1){
                    mAdapter=new itemsAdapter(produits);
                    grid.setAdapter(mAdapter);
                }else{
                    Log.e("t", ""+offset );
                    mAdapter.notifyDataSetChanged();
                }



            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(v.getContext(), SingleProductInfos.class);

                    intent.putExtra("page", page);
                    intent.putExtra("position", decalage+position);
                    intent.putExtra("url",url1);
                    intent.putExtra("url3",url2);
                    intent.putExtra("sorted",sorted);
                    intent.putExtra("type",type);
                    //le offset est utiliser dans le viewpager pour savoir quand est-ce que on atteint la fin du viewpager et
                    //faire un load more


                    // intent.putExtra("produits",produits);

                    startActivity(intent);
                }
            });






        }




    }

    private class itemsAdapter extends BaseAdapter{



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
        public Object getItem(int i) {
            return produits.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View row =inflater.inflate(R.layout.custom_grid_layout,parent,false);

            ImageView image=(ImageView) row.findViewById(R.id.thumbnail);
            TextView title=(TextView) row.findViewById(R.id.price);





            if(!produits.get(position).mediumImage.equals("null")){
                Picasso.with(getActivity().getApplicationContext())
                        .load(produits.get(position).mediumImage)
                        .into(image);

                title.setText(produits.get(position).salePrice);
                title.append("$");


            }else if(!produits.get(position).largeImage.equals("null")){
                Picasso.with(getActivity().getApplicationContext())
                        .load(produits.get(position).largeImage)
                        .into(image);

                title.setText(produits.get(position).salePrice);
                title.append("$");

            }else if(!produits.get(position).largerImage.equals("null")){
                Picasso.with(getActivity().getApplicationContext())
                        .load(produits.get(position).largerImage)
                        .into(image);


            }else{
                title.setVisibility(View.GONE);
            }


            return row;
        }
    }

    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}

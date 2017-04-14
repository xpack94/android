package com.example.xpack.bestbuy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xpack on 13/04/17.
 */

public class Grid extends Fragment {
    View v;
    private GridView horizontalGridView;
    customAdapter mAdapter;
    ProgressDialog progress;
    ArrayList<Products> produits = new ArrayList<Products>();
    String url1 = "https://api.bestbuy.com/v1/products(categoryPath.id=abcat0100000";
    String url2 = "*)?format=json&show=all&pageSize=25&page=";
    String url3 = "&apiKey=tghcgc6qnf72tat8a5kbja9r";
    int page = 2;
    LinearLayoutManager layoutManager;
    RecyclerView myList;
    yourAdapter adapt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.grid, container, false);
        progress = new ProgressDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        myList = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        myList.setLayoutManager(layoutManager);


        Fetcher f = new Fetcher();
        f.execute();

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


            try

            {
                Log.e("t", "doInBackground: " + url1 + url2 + page + url3);
                Parser.getProducts(produits, url1 + url2 + page + url3);

                //verifier si on est rendu au dernier produit
                //si oui le boutton load More deviendra invisible

            } catch (
                    IOException e) {
                e.printStackTrace();
            } catch (
                    JSONException e
                    )

            {
                e.printStackTrace();
            }


            return produits;
        }

        @Override
        protected void onPostExecute(final ArrayList<Products> prods) {
            progress.hide();
            //  gridViewSetting(horizontalGridView);
            // horizontalGridView.setNumColumns(prods.size());
//        mAdapter = new customAdapter(produits);
//        horizontalGridView.setAdapter(mAdapter);
            adapt=new yourAdapter(produits);
            myList.setAdapter(adapt);


        }
    }


    public class customAdapter extends BaseAdapter {


        ArrayList<Products> produits;

        public customAdapter(ArrayList<Products> produits) {
            super();
            this.produits = produits;
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

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.groups, parent, false);

            ImageView image = (ImageView) row.findViewById(R.id.image);


            if (produits.get(position).mediumImage != null) {
                Log.e("t", "getView: " + produits.get(position).mediumImage);
                Picasso.with(getActivity().getApplicationContext())
                        .load(produits.get(position).mediumImage)
                        .into(image);
            }

            return row;
        }
    }

//    private void gridViewSetting(GridView gridview) {
//
//
//        // Calculated single Item Layout Width for each grid element ....
//        int width =120 ;
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//
//        int totalWidth = (int) (width * 25 * density);
//        int singleItemWidth = (int) (width * density);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                120*25, LinearLayout.LayoutParams.MATCH_PARENT);
//
//        gridview.setLayoutParams(params);
//        gridview.setColumnWidth(120);
//        gridview.setHorizontalSpacing(2);
//        gridview.setStretchMode(GridView.STRETCH_SPACING);
//        gridview.setNumColumns(25);
//    }


    public class yourAdapter extends RecyclerView.Adapter<yourAdapter.SimpleViewHolder> {


        ArrayList<Products> prods;

        public yourAdapter(ArrayList<Products> produit) {
            prods=produit;
        }

        @Override
        public yourAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups, parent, false);
            return new yourAdapter.SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final yourAdapter.SimpleViewHolder holder, final int position) {
//            holder.name.setText(adap.getName());



            if(prods.get(position).mediumImage!=null){
                Picasso.with(getActivity().getApplicationContext())
                        .load(prods.get(position).mediumImage)
                        .into(holder.image);
            }



        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return 25;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            ImageView image;


            public  SimpleViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);


            }
        }

    }
}

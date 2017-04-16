package com.example.xpack.bestbuy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xpack.bestbuy.db.Favorite;
import com.squareup.picasso.Picasso;


public class FavoritesActivity extends Fragment {
    private static SQLiteDatabase db;

    ListView list;
    TextView title;
    View v;
    customAdapter adapter;
    static Cursor cur=null;
    int count=0;
    RelativeLayout noItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_favorites, container, false);

        noItem=(RelativeLayout) v.findViewById(R.id.noItem);
        list = (ListView) v.findViewById(R.id.list);
        // title = (TextView) findViewById(R.id.title);

        db = new DBHelper(getActivity()).getDB();

        Cursor c = Favorite.list(db);
        adapter=new customAdapter(getActivity(),c);


        count();




        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout layout= (LinearLayout) adapterView.getChildAt(i);
                LinearLayout lay=(LinearLayout) layout.getChildAt(1);
                TextView t=(TextView) lay.getChildAt(0);
                String textName=String.valueOf(t.getText());
                searchSku(textName);
            }
        });
        return v;
    }

    public class customAdapter extends CursorAdapter {
        private Cursor mCursor;
        private Context mContext;
        private final LayoutInflater mInflater;


        public customAdapter(Context context, Cursor c) {
            super(context, c);
            mInflater=LayoutInflater.from(context);
            mContext=context;
            cur=c;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView name=(TextView)view.findViewById(R.id.name);
            name.setText(cursor.getString(cursor.getColumnIndex("name")));
            ImageView image=(ImageView) view.findViewById(R.id.image);

            Picasso.with(getActivity())
                    .load(cursor.getString(cursor.getColumnIndex("image")))
                    .into(image);
            TextView salePrice=(TextView)view.findViewById(R.id.salePrice);
            salePrice.setText(cursor.getString(cursor.getColumnIndex(DBHelper.salePrice)));
            RatingBar ratings=(RatingBar) view.findViewById(R.id.ratings);
            ratings.setRating(Float.parseFloat(""+cursor.getColumnIndex(DBHelper.ratings)));
            TextView available=(TextView)view.findViewById(R.id.AvailablOnlineText);
            TextView removeFromWishList=(TextView) view.findViewById(R.id.addToWishList);
            removeFromWishList.setText("-");

            ImageView avail=(ImageView) view.findViewById(R.id.AvailablOnline);

            if(cursor.getString(cursor.getColumnIndex(DBHelper.available)).equals("true")){
                avail.setImageResource(R.drawable.available_24dp);
                available.setText(R.string.available_online);
                available.setTextColor(Color.parseColor("#2F4F4F"));
            }else{
                avail.setImageResource(R.drawable.ic_block_black_24dp);
                available.setText(R.string.not_available);
                available.setTextColor(Color.parseColor("#2F4F4F"));
            }
            removeFromWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Favorite.remove(db,cur.getString(cur.getColumnIndex("key")));
                    getActivity().recreate();

                }
            });







        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final View view=mInflater.inflate(R.layout.products,parent,false);
            return view;
        }

    }


    public void count(){
        SQLiteDatabase db=new DBHelper(getActivity()).getDB();

        Cursor c = Favorite.list(db);

        if(c.getCount()!=0){
            noItem.setVisibility(View.GONE);
        }else{
            noItem.setVisibility(View.VISIBLE);
        }


    }
    public void searchSku(String textName){
        SQLiteDatabase db=new DBHelper(getActivity()).getDB();
        Cursor c = Favorite.list(db);
        try {
            while (c.moveToNext()) {
                String n= c.getString(c.getColumnIndex("name"));
                if(n.equals(textName)){
                    String sku=c.getString(c.getColumnIndex("key"));
                    Intent intent=new Intent(getActivity(),SingleProductInfos.class);
                    String url1="https://api.bestbuy.com/v1/products/"+sku+".json?show=all&pageSize=1&page=";
                    String url2="&apiKey=tghcgc6qnf72tat8a5kbja9r";
                    intent.putExtra("page",1);
                    intent.putExtra("url",url1);
                    intent.putExtra("url3",url2);
                    intent.putExtra("offset",0);
                    intent.putExtra("sorted","false");
                    intent.putExtra("type","productSku");
                    intent.putExtra("startPosition",1);
                    startActivity(intent);
                }
            }
        } finally {
            c.close();
        }

    }


}
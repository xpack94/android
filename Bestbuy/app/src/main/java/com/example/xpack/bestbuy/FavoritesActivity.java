package com.example.xpack.bestbuy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_favorites, container, false);


        list = (ListView) v.findViewById(R.id.list);
        // title = (TextView) findViewById(R.id.title);

        db = new DBHelper(getActivity()).getDB();

        Cursor c = Favorite.list(db);
        adapter=new customAdapter(getActivity(),c);

        // title.setText("" + c.getCount() + " favorit(s)");


//
//        CursorAdapter adapter = new SimpleCursorAdapter(getActivity(),  android.R.layout.simple_list_item_2, c,
//                new String[]{"key", "added"},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                0);
//
//        list.setAdapter(adapter);

        list.setAdapter(adapter);
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
           // available.setText(cursor.getString(cursor.getColumnIndex(DBHelper.available)));

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





}
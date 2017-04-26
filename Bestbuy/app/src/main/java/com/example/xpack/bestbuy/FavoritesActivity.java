package com.example.xpack.bestbuy;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
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

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    // String cameback="CameBack";
                    Intent intent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                    return true;
                } else {
                    return false;
                }
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
        public void bindView(View view, Context context, final Cursor cursor) {

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
                        if (Settings.onOff){
                            showNotificationAdd(cur.getString(cur.getColumnIndex("name")),"product Deleted from wishList");
                        }

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
                    startActivityForResult(intent, 1);
                    onActivityResult(1, Activity.RESULT_OK, intent);
                }
            }
        } finally {
            c.close();
        }

    }


    //Invalidate list if wishlist is changed
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                Log.e("e", "called : " );
                Boolean changed = data.getExtras().getBoolean("changedFavorite");

                if (changed){
                    getActivity().recreate();
                }
            }
        }
    }

    public void showNotificationAdd(String productName,String content ) {
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(),WishList.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setTicker(r.getString(R.string.notifTitle))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(content)
                .setContentText(productName)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


}
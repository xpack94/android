package com.example.xpack.bestbuy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpack.bestbuy.db.Favorite;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xpack on 27/03/17.
 */

public class SingleProductFragment extends Fragment {

    Button addToWishList;
    private SQLiteDatabase db;
    LayoutInflater inflater;
    LinearLayout addDetails,castLayout,showCast;
    TextView detailsToggler,castToggler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_product, container, false);

        Bundle args=getArguments();

        final String name = args.getString("name");
        final String image= args.getString("largeImage");
        final String salePrice=args.getString("salePrice");
        String salesEnd=args.getString("salesEnd");
        final String ratings = args.getString("ratings");
        String ratingCount= args.getString("ratingCount");
        final String isAvailable=args.getString("isAvailable");
        String longDescription=args.getString("longDescription");
        final String addToCart = args.getString("addToCartUrl");
        final String sku=args.getString("sku");
        String details=args.getString("details");
        String cast=args.getString("cast");
        String regularPrice=args.getString("regularPrice");
        String dollarSavings=args.getString("dollarSavings");
        String shareUrl=args.getString("shareUrl");
        inflater=getActivity().getLayoutInflater();
        final Animation Toggle;
        final TextView ld,nameOfProduct;
        Toggle= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_down);
        Toggle.setDuration(2000);

        db = new DBHelper(getActivity()).getDB();

        nameOfProduct = (TextView) v.findViewById(R.id.name);
        nameOfProduct.setText(name);
        TextView Price=(TextView) v.findViewById(R.id.salePrice);
        final TextView toggle =(TextView) v.findViewById(R.id.toggle);


        TextView inStoreAvailability =(TextView) v.findViewById(R.id.isAvailable);
        LinearLayout clickable= (LinearLayout) v.findViewById(R.id.clickable);
        Button add_to_cart= (Button) v.findViewById(R.id.addToCart);
       // TextView endDate =(TextView) v.findViewById(R.id.salesEnd);

        Price.setText(salePrice);
        //endDate.setText("sales Ends : "+salesEnd);
        ld =(TextView) v.findViewById(R.id.longDescription);

        if(!longDescription.equals("null")){

            ld.setText(longDescription);

        }else{
            ld.setText(""+getResources().getString(R.string.noDescription));

        }

        ld.setVisibility(View.GONE);


        clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggle.getText().equals("+")) {
                    ld.setVisibility(View.VISIBLE);


                    toggle.setText("-");
                }else{
                    ld.setVisibility(View.GONE);
                    toggle.setText("+");
                }


            }
        });
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle.getText().equals("+")) {
                    ld.setVisibility(View.VISIBLE);


                    toggle.setText("-");
                }else{
                    ld.setVisibility(View.GONE);
                    toggle.setText("+");
                }

            }
        });

        ImageView largeImage = (ImageView) v.findViewById(R.id.largeFrontImage);

        RatingBar rt=(RatingBar) v.findViewById(R.id.ratings);
        LayerDrawable stars = (LayerDrawable) rt.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);


        if (isAvailable.equals("true")){
            inStoreAvailability.setText(getResources().getString(R.string.onLineOnly));
            add_to_cart.setEnabled(true);
            add_to_cart.setClickable(true);
        }else{
            inStoreAvailability.setText(getResources().getString(R.string.inStore));

        }

        if(!ratings.equals("null")) {
            rt.setRating(Float.parseFloat(ratings));
        }


        TextView ratingC = (TextView) v.findViewById(R.id.ratingCount);
        TextView rate=(TextView) v.findViewById(R.id.rate);

        if (!ratingCount.equals("null")){

            if(ratingCount.equals("1")){
                ratingC.setText("("+ratingCount+")"+" review");

            }else{
                ratingC.setText("("+ratingCount+")"+" reviews");
                rate.setText("("+Float.parseFloat(ratings)+")");
            }
        }else {
            ratingC.setText("0 review");

        }




        addToWishList=(Button) v.findViewById(R.id.addToWishList);
        if (Favorite.exists(db, sku)) {
            addToWishList.setText(R.string.removeFromWishList);
        }else{
            addToWishList.setText(R.string.addeToWishList);
        }


        addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (Favorite.exists(db, sku)) {
                        Favorite.remove(db, sku);
                        Toast.makeText(getActivity(), ""+getResources().getString(R.string.removedFromWishList), Toast.LENGTH_SHORT).show();
                        addToWishList.setText(R.string.addeToWishList);
                        if (Settings.onOff){
                            showNotificationAdd(name,"product Deleted from wishList");
                        }


                    } else {
                        Favorite.add(db, sku,name,image,salePrice,ratings,isAvailable);
                        Toast.makeText(getActivity(), ""+getResources().getString(R.string.addedToWishList), Toast.LENGTH_SHORT).show();
                        addToWishList.setText(R.string.removeFromWishList);
                        if (Settings.onOff){
                            showNotificationAdd(name,"product Added to wishList");

                        }
                    }

                    // Invalide les rows pour les faire se redessiner
                    if (isAdded()){
                        SingleProductInfos activity = (SingleProductInfos) getActivity();
                        activity.changedFavorite = true;
                    }


                }



        });


        Picasso.with(getActivity())
                .load(image)
                .into(largeImage);


        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Uri cart = Uri.parse(""+addToCart);
                Intent intent = new Intent(Intent.ACTION_VIEW, cart);
                startActivity(intent);

            }
        });



        try {
            JSONArray array = new JSONArray(details);
            addDetails=(LinearLayout) v.findViewById(R.id.addDetails);
            for (int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);

                addDetails.setVisibility(View.GONE);
                View view= inflater.inflate(R.layout.details,null);
                TextView t1=(TextView) view.findViewById(R.id.name);
                TextView t2=(TextView) view.findViewById(R.id.value);

                String dName=obj.getString("name");
                String dValue=obj.getString("value");
                t1.setText(dName);
                t2.setText(dValue);
                addDetails.addView(view);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            castLayout=(LinearLayout) v.findViewById(R.id.castAdd);
            showCast=(LinearLayout) v.findViewById(R.id.cast);
            JSONArray array = new JSONArray(cast);
            if(array.length()==0){

                showCast.setVisibility(View.GONE);
            }else{
                for (int i=0;i<array.length();i++){
                    JSONObject obj=array.getJSONObject(i);
                    View view=inflater.inflate(R.layout.details,null);
                    castLayout.setVisibility(View.GONE);
                    TextView t1=(TextView) view.findViewById(R.id.name);
                    TextView t2=(TextView) view.findViewById(R.id.value);
                    String dName=obj.getString("name");
                    String role=obj.getString("role");
                    t1.setText(dName);
                    t2.setText(role);
                    castLayout.addView(view);

                }
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        LinearLayout detailsClick=(LinearLayout) v.findViewById(R.id.details);
        detailsToggler=(TextView) v.findViewById(R.id.detailsToggler);
        detailsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(detailsToggler.getText()) .equals("+")){
                    addDetails.setVisibility(View.VISIBLE);
                    detailsToggler.setText("-");
                }else{
                    detailsToggler.setText("+");
                    addDetails.setVisibility(View.GONE);
                }

            }
        });

        //gerer les toggle de la section cast
        //cast est invisible si il y'a rien a l'interieur
        castToggler=(TextView) v.findViewById(R.id.castToggler);
        LinearLayout castClick=(LinearLayout) v.findViewById(R.id.castClick);
        castClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(String.valueOf(castToggler.getText()) .equals("+")){
                    castLayout.setVisibility(View.VISIBLE);
                    castToggler.setText("-");
                }else{
                    castToggler.setText("+");
                    castLayout.setVisibility(View.GONE);
                }
            }
        });

        LinearLayout l=(LinearLayout) v.findViewById(R.id.savings);
        if(Float.parseFloat(salePrice)==Float.parseFloat(regularPrice)){

            l.setVisibility(View.GONE);
        }else{

            TextView t=(TextView) v.findViewById(R.id.moneySaved);
            t.append(" "+dollarSavings+"$");
        }



        return v;




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

package com.example.xpack.bestbuy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.squareup.picasso.Picasso;

/**
 * Created by xpack on 27/03/17.
 */

public class SingleProductFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_product, container, false);

        Bundle args=getArguments();

        String name = args.getString("name");
        String image= args.getString("largeImage");
        String salePrice=args.getString("salePrice");
        String salesEnd=args.getString("salesEnd");
        String ratings = args.getString("ratings");
        String ratingCount= args.getString("ratingCount");
        String isAvailable=args.getString("isAvailable");
        String longDescription=args.getString("longDescription");
        final String addToCart = args.getString("addToCartUrl");
        final Animation Toggle;
        final TextView ld;
        Toggle= AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_down);
        Toggle.setDuration(2000);


        TextView nameOfProduct = (TextView) v.findViewById(R.id.name);
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
        ld.setText(longDescription);
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
            inStoreAvailability.setText("item available online");
            add_to_cart.setEnabled(true);
            add_to_cart.setClickable(true);
        }else{
            inStoreAvailability.setText("item only available in store");

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





        return v;




    }


}

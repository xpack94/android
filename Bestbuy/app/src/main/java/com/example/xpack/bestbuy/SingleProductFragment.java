package com.example.xpack.bestbuy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by xpack on 27/03/17.
 */

public class SingleProductFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_product, container, false);

        Bundle args=getArguments();

        String name = args.getString("name");
        String image= args.getString("largeImage");
        String salePrice=args.getString("salePrice");
        String salesEnd=args.getString("salesEnd");

        TextView nameOfProduct = (TextView) v.findViewById(R.id.name);
        nameOfProduct.setText(name);
        TextView Price=(TextView) v.findViewById(R.id.salePrice);
        TextView endDate =(TextView) v.findViewById(R.id.salesEnd);

        Price.setText(salePrice);
        endDate.setText("sales Ends : "+salesEnd);
        ImageView largeImage = (ImageView) v.findViewById(R.id.largeFrontImage);


        Picasso.with(getActivity())
                .load(image)
                .into(largeImage);
        return v;


    }


}

package com.example.xpack.bestbuy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Filter extends Activity implements View.OnClickListener {


    TextView currentOffers,Brands,Status,Category;
    static LayoutInflater inflater;
    static LinearLayout lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        inflater=getLayoutInflater();
        lin=(LinearLayout) findViewById(R.id.selection);
        currentOffers=  (TextView) findViewById(R.id.currentOffers);
        Brands=(TextView) findViewById(R.id.brands);
        Status =(TextView) findViewById(R.id.status);
        Category =(TextView) findViewById(R.id.category);

        currentOffers.setOnClickListener(this);
        Brands.setOnClickListener(this);
        Status.setOnClickListener(this);
        Category.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.currentOffers){
            CurrentOffersDialog current= new CurrentOffersDialog();
            current.show(getFragmentManager(),"dialog");

        }else if(view.getId()==R.id.brands){
            BrandsDialog brands=new BrandsDialog();
            brands.show(getFragmentManager(),"dialog");

        }else if(view.getId()==R.id.status){
            StatusDialog status=new StatusDialog();
            status.show(getFragmentManager(),"dialog");

        }else{

            CategoriesDialog cat = new CategoriesDialog();
            cat.show(getFragmentManager(),"dialog");


           // lin.addView(getView(CategoriesDialog.mSelectedItems.get(0)));

        }



    }

    public static View getView(String s){
        View rootView = inflater.inflate(R.layout.filtring_rows, null);
        ImageView image = (ImageView) rootView.findViewById(R.id.delete);
        TextView text=(TextView) rootView.findViewById(R.id.chosen_option);
        text.setText(s);

        return rootView;
    }
}

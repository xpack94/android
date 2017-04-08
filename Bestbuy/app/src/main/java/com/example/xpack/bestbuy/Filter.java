package com.example.xpack.bestbuy;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Filter extends Activity implements View.OnClickListener {


    TextView currentOffers,Brands,Status,Category;
    static LayoutInflater inflater;
    static LinearLayout lin;
    BrandsDialog brands;
    StatusDialog status;
    CategoriesDialog cat;
    CurrentOffersDialog current;
    FragmentManager f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        inflater=getLayoutInflater();
        f=getFragmentManager();
        brands=new BrandsDialog();
        current= new CurrentOffersDialog();
        status=new StatusDialog();
        cat = new CategoriesDialog();
        lin=(LinearLayout) findViewById(R.id.selection);
        currentOffers=  (TextView) findViewById(R.id.currentOffers);
        Brands=(TextView) findViewById(R.id.brands);
        Status =(TextView) findViewById(R.id.status);
        Category =(TextView) findViewById(R.id.category);
        Category.setOnClickListener(this);
        currentOffers.setOnClickListener(this);
        Brands.setOnClickListener(this);
        Status.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.currentOffers){

            current.show(f,"dialog");

        }else if(view.getId()==R.id.brands){
            brands.show(f,"dialog");
        }else if(view.getId()==R.id.status){

            status.show(f,"dialog");

        }else{

            cat.show(f,"dialog");




        }



    }

    public static View getView(String s,String tag){
        View rootView = inflater.inflate(R.layout.filtring_rows, null);
        ImageView image = (ImageView) rootView.findViewById(R.id.delete);
        TextView text=(TextView) rootView.findViewById(R.id.chosen_option);
        image.setTag(tag);
        text.setTag(tag);
        text.setText(s);

        return rootView;
    }
    //cette methode est appelé lorsque l'image ou le text de la selection actuel sont cliqué
    public void delete(View v){

        String tag=(String) v.getTag();
        LinearLayout l=(LinearLayout)v.getParent();
        TextView t=(TextView) l.getChildAt(1);
        CharSequence itemToDelete=t.getText();
        lin.removeView((LinearLayout) v.getParent());
        del(tag,itemToDelete);

    }

    private void del(String tag,CharSequence itemToDelete){

        ArrayList<String> e=new ArrayList<String>();
        ArrayList<Integer> f=new ArrayList<Integer>();
        if(tag.equals("status")){
           e=status.items;
            f=status.mSelectedItems;
            StatusDialog.size--;
        }else if(tag.equals("offers")){
            e=current.items;
            f=current.mSelectedItems;
            CurrentOffersDialog.size--;

        }else if(tag.equals("brands")) {
            e=brands.items;
            f=brands.mSelectedItems;
            BrandsDialog.size--;
        }else{
            Log.e("t", "entrered:"  );
            LinearLayout l=(LinearLayout) findViewById(R.id.categories);
            View v=(View) findViewById(R.id.cat);
            l.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
        }

        for(int i=0;i<e.size();i++){
            if(e.get(i).equals(itemToDelete)){
                e.remove(i);
                f.remove(i);

            }
        }
    }


}

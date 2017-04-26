package com.example.xpack.bestbuy;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Filter extends Activity implements View.OnClickListener {


    TextView currentOffers,Brands,Status,Category,salePrice;
    static LayoutInflater inflater;
    static LinearLayout lin,priceLayout,catLayout;
    BrandsDialog brands;
    StatusDialog status;
    CategoriesDialog cat;
    CurrentOffersDialog current;
    PriceDialog price;
    FragmentManager f;
    EditText priceMin,priceMax;
    ImageView reset;
    TextView resetAll;
    View catLine,priceLine;
    Button done;
    ImageView currentAdd,statusAdd,categoryAdd,priceAdd,brandsAdd;
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
        price=new PriceDialog();
        lin=(LinearLayout) findViewById(R.id.selection);
        currentOffers=  (TextView) findViewById(R.id.currentOffers);
        Brands=(TextView) findViewById(R.id.brands);
        Status =(TextView) findViewById(R.id.status);
        Category =(TextView) findViewById(R.id.category);
        categoryAdd=(ImageView) findViewById(R.id.categoryAdd);
        currentAdd=(ImageView) findViewById(R.id.currentOffersAdd);
        brandsAdd=(ImageView) findViewById(R.id.brandsAdd);
        statusAdd=(ImageView) findViewById(R.id.statusAdd);
        priceAdd=(ImageView) findViewById(R.id.priceAdd);

        salePrice=(TextView) findViewById(R.id.salePriceAdd);
        priceMin=(EditText) findViewById(R.id.priceMin);
        priceMax=(EditText) findViewById(R.id.priceMax);
        reset=(ImageView) findViewById(R.id.reset);
        resetAll=(TextView) findViewById(R.id.resetAll);
        catLayout=(LinearLayout) findViewById(R.id.categories);
        priceLayout=(LinearLayout) findViewById(R.id.price);


        catLine=(View ) findViewById(R.id.cat);
        priceLine=(View ) findViewById(R.id.pri);
        done=(Button) findViewById(R.id.done);
        Category.setOnClickListener(this);
        currentOffers.setOnClickListener(this);
        Brands.setOnClickListener(this);
        Status.setOnClickListener(this);
        salePrice.setOnClickListener(this);

        reset.setOnClickListener(this);
        resetAll.setOnClickListener(this);
        done.setOnClickListener(this);

        priceAdd.setOnClickListener(this);
        currentAdd.setOnClickListener(this);
        statusAdd.setOnClickListener(this);
        categoryAdd.setOnClickListener(this);
        brandsAdd.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.currentOffers ||view.getId()==R.id.currentOffersAdd){

            current.show(f,"dialog");


        }else if(view.getId()==R.id.brands || view.getId()==R.id.brandsAdd ){
            brands.show(f,"dialog");

        }else if(view.getId()==R.id.status || view.getId()==R.id.statusAdd ){

            status.show(f,"dialog");

        }else if(view.getId()==R.id.salePriceAdd || view.getId()==R.id.priceAdd ){
                price.show(f,"dialog");

           //reset all est cliqué
        }else if(view.getId()==R.id.reset || view.getId()==R.id.resetAll){
            resetSlections();

        }else if(view.getId()==R.id.category || view.getId()==R.id.categoryAdd ){
            cat.show(f,"dialog");
        }else{
            //button done est cliqué
            String url=makeUrl();

            if (url.equals("https://api.bestbuy.com/v1/products(")){
                url="https://api.bestbuy.com/v1/products?format=json&show=all&pageSize=25&page=";
            }else{
                url+=")?format=json&show=all&pageSize=25&page=";
            }

            String url2="&apiKey=tghcgc6qnf72tat8a5kbja9r";

            Intent intent=new Intent(Filter.this,AllProducts.class);
            intent.putExtra("url1",url);
            intent.putExtra("url2",url2);
            intent.putExtra("page",1);
            intent.putExtra("decalage",0);
            intent.putExtra("type","");
            startActivity(intent);

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
    //cette methode permet de supprimer une selection faite par l'utilisateur en cliqué sur celle ci
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
        }else if(tag.equals("price")){
            priceLayout.setVisibility(View.VISIBLE);
            priceLine.setVisibility(View.VISIBLE);

        }else {

            catLayout.setVisibility(View.VISIBLE);
            catLine.setVisibility(View.VISIBLE);
        }

        for(int i=0;i<e.size();i++){
            if(e.get(i).equals(itemToDelete)){
                e.remove(i);
                f.remove(i);

            }
        }

    }
    //cette methode est appelé lorsque reset est appuyer
    //elle permet d'effacer la selection que l'utilisateur a fait et recommencer a zero
    private void resetSlections(){
        int co=lin.getChildCount();
        for (int i = 0; i < co; i++) {
            View v = lin.getChildAt(0);

            lin.removeView(v);

        }
        current=new CurrentOffersDialog();
        status=new StatusDialog();
        brands=new BrandsDialog();

        Log.e("t" ," "+String.valueOf(priceLayout.getVisibility()==View.GONE));
        //rendre la section prix et category visible si il etait invisible
        if(priceLayout.getVisibility()==View.GONE){
            priceLayout.setVisibility(View.VISIBLE);
            priceLine.setVisibility(View.VISIBLE);
        }
        if(catLayout.getVisibility()==View.GONE){
            catLayout.setVisibility(View.VISIBLE);
            catLine.setVisibility(View.VISIBLE);
        }
    }

    private String makeUrl(){
        int co=lin.getChildCount();
        String url="https://api.bestbuy.com/v1/products(";
        for (int i=0;i<lin.getChildCount();i++){
            LinearLayout l=(LinearLayout) lin.getChildAt(i);
            TextView t=(TextView) l.getChildAt(1);
            String tag=(String)t.getTag();
            if(tag.equals("status")){
                url+="new=true";

            }else if(tag.equals("offers")){
               url=makeurlForOffers(url,t);
            }else if(tag.equals("brands")){
                CharSequence brand=t.getText();
                url+="manufacturer="+brand;
            }else if(tag.equals("price")){
                String s=""+t.getText();
                s=s.replace(" ","&");
                url+=s;
            }else{
                String r=""+t.getText();
                r=r.replace("and"," ");
                url+="categoryPath.name="+r+"*";
            }
            if(co!=1){
                url+="&";
                co--;
            }
        }


        return url;
    }

    private String makeurlForOffers(String u,TextView t){
            if(t.getText().equals("ON Sale")){
                u+="onSale=true";
            }else if(t.getText().equals("On Clearance")){
                u+="clearance=true";
            }else if(t.getText().equals("Available Online")){
                u+="onlineAvailability=true";
            }else{
                u+="inStoreAvailability=true";
            }

        return u;
    }
}

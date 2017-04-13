package com.example.xpack.bestbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by xpack on 12/04/17.
 */

public class MyLists extends Fragment implements View.OnClickListener {

    View v;
    FloatingActionButton add;
    LayoutInflater inflater;
    LinearLayout inlay;
    RelativeLayout parent;
    TextView t;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.my_lists, container, false);

        inlay=(LinearLayout) v.findViewById(R.id.addNewWishList);
        add=(FloatingActionButton) v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),CreatingNewList.class);
                i.putExtra("text","");
                startActivityForResult(i, 1);

              //  onActivityResult(1, RESULT_OK, i);
            }
        });





        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful

            if (resultCode == Activity.RESULT_OK) {

                String pressed=data.getExtras().getString("added");
                String text=data.getExtras().getString("text");

                if(pressed.equals("add")){
                    inflater=getActivity().getLayoutInflater();
                    View f= inflater.inflate(R.layout.wish_list_line,null);
                    t=(TextView) f.findViewById(R.id.wishListName);
                    ImageView edit=(ImageView) f.findViewById(R.id.edit);
                    ImageView delete=(ImageView) f.findViewById(R.id.delete);
                    delete.setOnClickListener(this);
                    edit.setOnClickListener(this);
                    t.setText(text);
                    inlay.addView(f);
                }

            }
        }else if(requestCode==2){

            if (resultCode == Activity.RESULT_OK) {
                String pressed=data.getExtras().getString("added");
                String text=data.getExtras().getString("text");
                if(pressed.equals("add")){
                    TextView tex=(TextView) parent.getChildAt(0);
                    tex.setText(text);

                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.edit){
            parent=(RelativeLayout)view.getParent();
            Intent i= new Intent(getActivity(),CreatingNewList.class);
            i.putExtra("text", String.valueOf(t.getText()));
            startActivityForResult(i, 2);
        }else{
            parent=(RelativeLayout) view.getParent();
            inlay.removeView(parent);
            getActivity().recreate();
        }
    }
}

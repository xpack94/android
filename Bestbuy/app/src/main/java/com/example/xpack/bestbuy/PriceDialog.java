package com.example.xpack.bestbuy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by xpack on 08/04/17.
 */

public class PriceDialog  extends DialogFragment {

    EditText priceMin,priceMax;
    View v;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
         v=(inflater.inflate(R.layout.price_dialog_box, null));
                // Add action buttons
               builder.setView(v)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        priceMin=(EditText) v.findViewById(R.id.priceMin);
                        priceMax=(EditText) v.findViewById(R.id.priceMax);
                        String salePrice=getResources().getString(R.string.salePrice);
                        String pmin=priceMin.getText().toString();
                        String pmax=priceMax.getText().toString();
                        if(!pmin.equals("") || !pmax.equals("")){
                            if(!pmin.equals("") && pmax.equals("")){
                                Filter.lin.addView(Filter.getView(salePrice+">"+priceMin.getText().toString(),"price"));
                            }else if(pmin.equals("") && !pmax.equals("")){

                                Filter.lin.addView(Filter.getView(salePrice+"<"+priceMax.getText().toString(),"price"));
                            }else{
                                Filter.lin.addView(Filter.getView(salePrice+">"+priceMin.getText().toString()
                                        +" "+salePrice+"<"+priceMax.getText().toString(),"price"));
                            }
                        }
                        LinearLayout price=(LinearLayout) getActivity().findViewById(R.id.price);
                        View v=(View ) getActivity().findViewById(R.id.pri);
                        price.setVisibility(View.GONE);
                        v.setVisibility(View.GONE);

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //  LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

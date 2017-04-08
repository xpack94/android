package com.example.xpack.bestbuy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;


public class StatusDialog extends DialogFragment {


    ArrayList <Integer> mSelectedItems;
    ArrayList<String> items;
    static int size;
    StatusDialog(){
        this.mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
        this.size=mSelectedItems.size();
        this.items=new ArrayList<String>();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title


        builder.setTitle(R.string.categories_dialog)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected

                .setMultiChoiceItems(R.array.array_of_status, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    if(!search(which)){
                                        mSelectedItems.add(which);
                                        String w=getResources().getStringArray(R.array.array_of_status)[which];
                                        items.add(w);
                                    }
                                    // If the user checked the item, add it to the selected items

                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        for (int i=size;i<mSelectedItems.size();i++){
                            int s=mSelectedItems.get(i);
                            Filter.lin.addView(Filter.getView(getResources().getStringArray(R.array.array_of_status)[s],"status"));
                            size=mSelectedItems.size();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private Boolean search(int i){

        for(int s=0;s<mSelectedItems.size();s++){
            if(mSelectedItems.get(s)==i){
                return true;
            }
        }
        return false;
    }

}

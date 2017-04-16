package com.example.xpack.bestbuy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by xpack on 01/04/17.
 */

public class DefaultDialog extends DialogFragment {

    String listName;
     public DefaultDialog(String listName){
        this.listName=listName;
    }

    static Boolean clicked=false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.makeDefault);
        dialog.setMessage(R.string.makeDefaultMessage);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               MyLists.name=listName;

            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               
            }
        });

        return dialog.create();
    }

}

package com.example.xpack.bestbuy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;

/**
 * Created by xpack on 01/04/17.
 */

public class NoInternet extends DialogFragment {

   static Boolean clicked=false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.internet);
        dialog.setMessage(R.string.internetMessage);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WifiManager wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
                clicked=true;

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        return dialog.create();
    }

}

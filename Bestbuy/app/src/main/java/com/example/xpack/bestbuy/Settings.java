package com.example.xpack.bestbuy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;


public class Settings extends Activity {
    Switch switcher;
    static boolean onOff=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        switcher=(Switch) findViewById(R.id.switch1);
        switcher.setChecked(onOff);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public  void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
               onOff=isChecked;

            }
        });



    }


    }

package com.example.xpack.bestbuy;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parser.setContext(getApplicationContext());
    }
}

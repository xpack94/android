package com.example.xpack.bestbuy;

import org.json.JSONArray;

/**
 * Created by xpack on 23/04/17.
 */

public class Stores  {

    String name;
    String longName;
    String address1;
    String addres2;
    JSONArray services;
    JSONArray hours;
    String phone;


    public Stores(String name,String longName,String address1,String addres2,JSONArray services,JSONArray hours,String phone){
        this.name=name;
        this.longName=longName;
        this.address1=address1;
        this.addres2=addres2;
        this.services=services;
        this.hours=hours;
        this.phone=phone;

    }



}

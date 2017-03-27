package com.example.xpack.bestbuy;

/**
 * Created by xpack on 18/03/17.
 */

public class Products {

    public String url;
    public String name;
    public String salePrice;
    public String customerReview;


    public Products(String sku,String name,String url,String customerReview){
        this.salePrice=sku;
        this.name=name;
        this.url=url;
        this.customerReview=customerReview;
    }
}

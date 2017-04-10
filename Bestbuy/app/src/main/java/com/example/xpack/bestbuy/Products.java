package com.example.xpack.bestbuy;


public class Products {

    public String url;
    public String name;
    public String salePrice;
    public String customerReview;
    public String largeImage;
    public String mediumImage;
    public String salesEnd;
    public String ratingCount;
    public String isAvailable;
    public String longDescription;
    public String addToCartUrl;
    public String largerImage;



    public Products(String sku, String name, String url, String customerReview, String largeImage, String salesEnd, String ratingCount,
                    String isAvailable, String longDescription,String addToCartUrl,String mediumImage,String largerImage){
        this.salePrice=sku;
        this.name=name;
        this.url=url;
        this.customerReview=customerReview;
        this.largeImage=largeImage;
        this.salesEnd=salesEnd;
        this.ratingCount=ratingCount;
        this.isAvailable=isAvailable;
        this.longDescription=longDescription;
        this.addToCartUrl=addToCartUrl;
        this.mediumImage=mediumImage;
        this.largerImage=largerImage;
    }
}

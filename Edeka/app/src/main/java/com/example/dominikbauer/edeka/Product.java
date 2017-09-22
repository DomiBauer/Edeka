package com.example.dominikbauer.edeka;

public class Product {

    public int id;
    public String productName;
    public String productDescription;
    public double originalPrice;
    public double discountPrice;
    public String imgURL;
    public boolean discount;
    public boolean onShoppingList;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(int id, String productName, String productDescription, double originalPrice, double discountPrice, String imgURL, boolean discount, boolean onShoppingList) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.imgURL = imgURL;
        this.discount = discount;
        this.onShoppingList = onShoppingList;
    }
}

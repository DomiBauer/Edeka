package com.example.dominikbauer.edeka;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    public int id;
    public String productName;
    public String productDescription;
    public double originalPrice;
    public double discountPrice;
    public String imgURL;
    public boolean discount;
    public boolean onShoppingList;
    public int location;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(int id, String productName, String productDescription, double originalPrice, double discountPrice, String imgURL, boolean discount, boolean onShoppingList, int location) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.imgURL = imgURL;
        this.discount = discount;
        this.onShoppingList = onShoppingList;
        this.location = location;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        productName = in.readString();
        productDescription = in.readString();
        originalPrice = in.readDouble();
        discountPrice = in.readDouble();
        imgURL = in.readString();
        discount = in.readByte() != 0;
        onShoppingList = in.readByte() != 0;
        location = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(productName);
        parcel.writeString(productDescription);
        parcel.writeDouble(originalPrice);
        parcel.writeDouble(discountPrice);
        parcel.writeString(imgURL);
        parcel.writeByte((byte) (discount ? 1 : 0));
        parcel.writeByte((byte) (onShoppingList ? 1 : 0));
        parcel.writeInt(location);
    }
}

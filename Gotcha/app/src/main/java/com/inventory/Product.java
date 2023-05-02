package com.seller.inventory;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Product implements Parcelable {

    private String mName;
    private String mType;
    private String mSize;
    private String mBrand;


    private double mPrice;

    public Product(String brand, String name, double price, String type, String size) {
        mBrand = brand;
        mName = name;
        mPrice = price;
        mSize = size;
        mType = type;
    }

    protected Product(Parcel in) {
        mBrand = in.readString();
        mName = in.readString();
        mPrice = in.readDouble();
        mSize = in.readString();
        mType = in.readString();

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

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getSize() {
        return mSize;
    }
    public String getmBrand() {
        return mBrand;
    }
    public double getPrice() {
        return mPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mType);
        parcel.writeString(mSize);
        parcel.writeString(mBrand);
        parcel.writeDouble(mPrice);
    }
}

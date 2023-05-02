package com.orders;

import android.os.Parcel;
import android.os.Parcelable;


public class Orders implements Parcelable {

    private String mSeller;
    private String mItem;

    public Orders(String seller, String item) {
        mSeller = seller;
        mItem = item;
    }

    protected Orders(Parcel in) {
        mSeller = in.readString();
        mItem = in.readString();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };


    public String getmSeller() {
        return mSeller;
    }
    public String getmItem() {
        return mItem;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSeller);
        parcel.writeString(mItem);

    }
}
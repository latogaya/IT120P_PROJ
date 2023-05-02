package com.orders;

import android.os.Parcel;
import android.os.Parcelable;


public class Feedbacks implements Parcelable {

    private String mSeller;
    private String mItem;

    public Feedbacks(String seller, String item) {
        mSeller = seller;
        mItem = item;

    }

    protected Feedbacks(Parcel in) {
        mSeller = in.readString();
        mItem = in.readString();

    }

    public static final Creator<Feedbacks> CREATOR = new Creator<Feedbacks>() {
        @Override
        public Feedbacks createFromParcel(Parcel in) {
            return new Feedbacks(in);
        }

        @Override
        public Feedbacks[] newArray(int size) {
            return new Feedbacks[size];
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

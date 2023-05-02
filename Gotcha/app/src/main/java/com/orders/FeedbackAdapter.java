package com.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.products.R;

import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<Orders> {
    private Context mContext;
    private int mResource;
    private List<Orders> mProducts;



    public FeedbackAdapter(Context context, int resource, List<Orders> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mProducts = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Orders product = getItem(position);

        TextView nameTextView = view.findViewById(R.id.sellerFeedback);
        nameTextView.setText(product.getmSeller());

        TextView descriptionTextView = view.findViewById(R.id.itemFeedback);
        descriptionTextView.setText(product.getmItem());

        return view;
    }
}
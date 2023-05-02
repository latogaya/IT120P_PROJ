package com.inventory;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.products.R;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends ArrayAdapter<com.seller.inventory.Product> {
    private Context mContext;
    private int mResource;
    private List<com.seller.inventory.Product> mProducts;

    public ProductAdapter(Context context, int resource, List<com.seller.inventory.Product> objects) {
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

        com.seller.inventory.Product product = mProducts.get(position);

        TextView brandTextView = view.findViewById(R.id.productBrand);
        TextView nameTextView = view.findViewById(R.id.productName);
        TextView priceTextView = view.findViewById(R.id.productPrice);
        TextView sizeTextView = view.findViewById(R.id.productSize);
        TextView typeTextView = view.findViewById(R.id.productType);

        brandTextView.setText(product.getmBrand());
        nameTextView.setText(product.getName());
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
        sizeTextView.setText(product.getSize());
        typeTextView.setText(product.getType());

        return view;
    }
}
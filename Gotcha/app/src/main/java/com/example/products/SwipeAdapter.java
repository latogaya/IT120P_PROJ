package com.example.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.util.List;

public class SwipeAdapter extends BaseAdapter{

    private Context context;
    private List<Integer> list;

    public SwipeAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {return 3;}

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {return i;}

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_front, parent, false);

        }else {
            view = convertView;
        }

        return view;

    }

}



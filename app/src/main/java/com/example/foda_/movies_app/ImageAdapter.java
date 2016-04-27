package com.example.foda_.movies_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by foda_ on 2016-03-25.
 */
public class ImageAdapter extends BaseAdapter {
    public Context myContext;
    ArrayList<Movies> item;
    public ImageAdapter(Context context, ArrayList<Movies> item) {
        myContext = context;
        this.item=item;
    }

    public int getCount() {
        return item.size();
    }
    public Object getItem(int position) {
        return   getItem(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(myContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView=imageView;
            imageView.setPadding(5, 5, 5, 5);
        }
        else
            imageView = (ImageView)convertView;

        Picasso.with(myContext)
                .load(item.get(position).JsonPath)
                .resize(400, 700).into((ImageView)convertView);

        return convertView;

    }
}
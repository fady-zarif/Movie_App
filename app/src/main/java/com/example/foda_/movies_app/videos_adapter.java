package com.example.foda_.movies_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by foda_ on 2016-04-01.
 */
public class videos_adapter extends BaseAdapter {
    int []item;
    Context myContext;
 public videos_adapter(Context mycontext,int[]item)
 {
     this.myContext=mycontext;
     this.item=item;
 }
    @Override
    public int getCount() {
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        TextView text;
        LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(myContext.LAYOUT_INFLATER_SERVICE);
        View Row;
        Row= inflater.inflate(R.layout.imageview,parent,false);
        img=(ImageView)Row.findViewById(R.id.image_);
        img.setImageResource(item[position]);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(220,190);
        params.setMargins(10,0,10,0);
        img.setLayoutParams(params);
        text=(TextView)Row.findViewById(R.id.text_);
        text.setText("Trailer "+(position+1));

        return Row;
    }
}

package com.example.assignment;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import android.widget.GridView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int[] pos;
    private int[] drawable;
    boolean status = false;
    int gridposition = -1;

    public ImageAdapter(Context context, int[] pos, int[] drawable) {
        this.context = context;
        this.pos = pos;
        this.drawable = drawable;
    }

    @Override
    public int getCount() {
        return pos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if (position == gridposition && status) {
            imageView.setImageResource(drawable[pos[gridposition]]);
            Log.d("input position ID:", String.valueOf(gridposition));
        } else {
            imageView.setImageResource(R.drawable.hidden);
        }

        return imageView;
    }


    public void displayCard(final int index) {
        gridposition = index;
        //   gridposition = 1;
        status = true;
        Log.d("Display card position:", String.valueOf(index) + " | " + String.valueOf(gridposition));
        this.getView(gridposition, null, null);
    }
}
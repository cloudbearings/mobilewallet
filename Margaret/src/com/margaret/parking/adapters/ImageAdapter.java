package com.margaret.parking.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.margaret.parking.R;

/**
 * Created by varmu02 on 6/20/2015.
 */
public class ImageAdapter extends ArrayAdapter {
    Context mContext;
    int mResourceLayout;
    List<Bitmap> mBitmapList;
    public ImageAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
        this.mResourceLayout = resource;
        this.mBitmapList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.photogrid_item, null);
        }
       ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(mBitmapList.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return mBitmapList.size();
    }
}

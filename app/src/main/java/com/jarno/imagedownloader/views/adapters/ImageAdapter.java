package com.jarno.imagedownloader.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jarno.imagedownloader.utils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jarno on 30.4.2016.
 */
public class ImageAdapter  extends BaseAdapter {
    private Context mContext;
    // references to our images
    private ArrayList<File> thumbs;

    public ImageAdapter(Context c, ArrayList<File> thumbs) {
        mContext = c;
        this.thumbs = thumbs;
    }

    public int getCount() {
        return thumbs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(BitmapUtils.loadBitmap(thumbs.get(position).getAbsolutePath()));
        return imageView;
    }
}
package com.lawba.portalberita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lawba.portalberita.R;
import com.lawba.portalberita.model.News;

import java.util.ArrayList;

/**
 * Created by TRIPOD STUDIO on 9/25/2016.
 */

public class NewsAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<News> newses ;
    private LayoutInflater inflater = null ;

    public NewsAdapter(Context context, ArrayList<News> newses) {
        this.context = context;
        this.newses = newses;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return newses.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView ;

        if (convertView == null) {

            v = inflater.inflate(R.layout.list_berita, null);

            TextView judul = (TextView) v.findViewById(R.id.txtjdl);
            ImageView thumb_image = (ImageView) v.findViewById(R.id.imageView);

            News news = newses.get(position);

            judul.setText(news.getJudul());
            Glide.with(context)
                    .load(news.getGambar())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumb_image);
        }

        return v ;
    }
}

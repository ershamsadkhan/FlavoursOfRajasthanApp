package com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.ImageDownloaderTask;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.R;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ItemDto> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList<ItemDto> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemDto newsItem = listData.get(position);
        holder.headlineView.setText(newsItem.ItemHeader);
        holder.reporterNameView.setText(newsItem.ItemDescription);
        holder.reportedDateView.setText(""+newsItem.FullPrice);

        if (newsItem.getImage() != null) {
            holder.imageView.setImageBitmap(newsItem.getImage());
        } else {
            // MY DEFAULT IMAGE
            holder.imageView.setImageResource(R.drawable.dummy_product_image);
        }

        /*if (holder.imageView != null) {
            new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
        }*/

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
        ImageView imageView;
    }
}

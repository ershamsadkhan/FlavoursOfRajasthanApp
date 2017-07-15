package com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.OrderFragment;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.R;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDtoForOrder;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ItemDto> listData;
    private LayoutInflater layoutInflater;

    FragmentManager fragmentManager;

    Context context;
    ItemDtoForOrder itemDtoForOrder;
    public CustomListAdapter(Context context, ArrayList<ItemDto> listData) {
        this.listData = listData;
        Log.e("context FOR",context.toString());
        layoutInflater = LayoutInflater.from(context);

        this.context=context;
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
            holder.itemHeader = (TextView) convertView.findViewById(R.id.item_header);
            holder.itemDescription = (TextView) convertView.findViewById(R.id.item_description);
            holder.itemRate = (TextView) convertView.findViewById(R.id.rate);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.btnOrder = (Button) convertView.findViewById(R.id.btn_item_selected);
            holder.btnOrder.setOnClickListener(myButtonClickListener);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemDto newsItem = listData.get(position);
        holder.itemHeader.setText(newsItem.ItemHeader);
        holder.itemDescription.setText(newsItem.ItemDescription);
        holder.itemRate.setText("" + newsItem.FullPrice);

        if (newsItem.getImage() != null) {
            holder.imageView.setImageBitmap(newsItem.getImage());
        } else {
            // MY DEFAULT IMAGE
            holder.imageView.setImageResource(R.drawable.dummy_product_image);
        }

        return convertView;
    }


    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            View parentRow = (View) v.getParent();
            LinearLayout l1=(LinearLayout)parentRow.getParent();
            LinearLayout l2=(LinearLayout)l1.getParent();
            LinearLayout l3=(LinearLayout)l2.getParent();
            ListView listView = (ListView) l3.getParent();
            final int position = listView.getPositionForView(parentRow);


            ItemDto selectedItem = (ItemDto) listView.getItemAtPosition(position);

            itemDtoForOrder=new ItemDtoForOrder();
            itemDtoForOrder.image=selectedItem.getImage();
            itemDtoForOrder.Categoryid=selectedItem.Categoryid;
            itemDtoForOrder.FullPrice=selectedItem.FullPrice;
            itemDtoForOrder.HalfPrice=selectedItem.HalfPrice;
            itemDtoForOrder.QuaterPrice=selectedItem.QuaterPrice;
            itemDtoForOrder.ItemDescription=selectedItem.ItemDescription;
            itemDtoForOrder.ItemHeader=selectedItem.ItemHeader;
            itemDtoForOrder.Itemid=selectedItem.Itemid;
            itemDtoForOrder.ImageUrl=selectedItem.ImageUrl;

            Bundle b=new Bundle();
            b.putSerializable("ItemDto",itemDtoForOrder);
            Fragment fragment=new OrderFragment();
            fragment.setArguments(b);

            fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

            int count = fragmentManager.getBackStackEntryCount();
            for(int i = 0; i < count; ++i) {
                //fragmentManager.popBackStack();
            }
            //fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent,fragment)
                    .addToBackStack("GotoHome")
                    .commit();
            fragmentManager.executePendingTransactions();


        }
    };

    static class ViewHolder {
        TextView itemHeader;
        TextView itemDescription;
        TextView itemRate;
        ImageView imageView;
        Button btnOrder;
    }
}

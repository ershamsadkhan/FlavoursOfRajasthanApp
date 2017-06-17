package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomProgress;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.ImageCache;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDtoForOrder;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderLineItemDto;
import com.google.gson.Gson;

/**
 * Created by SAM on 6/4/2017.
 */

public class CartFragment extends Fragment {

    LinearLayout linearLayout;
    View rootView;
    ItemDto item;
    ItemDtoForOrder itemDtoForOrder;
    OrderDto orderDto;
    OrderLineItemDto orderLineItemDto;

    TextStorage txtStorage;
    Gson gson;
    Alert alert;
    CustomProgress customProgress;

    ImageCache imageCache;

    Button btnBuy;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.cart_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.mainLayout);
        loadCartView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        alert = new Alert(getActivity());
        customProgress = new CustomProgress(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));
        imageCache = ImageCache.getInstance();

        btnBuy=(Button)getActivity().findViewById(R.id.btn_buy);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        txtStorage = new TextStorage(getActivity());
        gson = new Gson();

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ConfirmOrderFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });

        getActivity().setTitle("Menu 1");
    }

    public void loadCartView() {
        String tempOrderDto = txtStorage.getCartData();
        if (tempOrderDto == "") {
            alert.alertMessage("No Items present in the cart");
        } else {
            orderDto = gson.fromJson(tempOrderDto, OrderDto.class);
        }

        for (int i = 0; i < orderDto.OrderLineItemList.size(); i++) {
            OrderLineItemDto tempOrderLineItemDto = orderDto.OrderLineItemList.get(i);
            View hiddenInfo = getActivity().getLayoutInflater().inflate(R.layout.cart_item, linearLayout, false);
            Button demobtn = (Button) hiddenInfo.findViewById(R.id.btn_edit);
            ImageView itemImage = (ImageView) hiddenInfo.findViewById(R.id.thumbImage);
            TextView itemHeader = (TextView) hiddenInfo.findViewById(R.id.item_header);
            TextView itemQty = (TextView) hiddenInfo.findViewById(R.id.itemqty);
            TextView itemType = (TextView) hiddenInfo.findViewById(R.id.itemtype);
            TextView itemAmount = (TextView) hiddenInfo.findViewById(R.id.itemamt);

            setImage(itemImage, tempOrderLineItemDto.ImageUrl);
            setType(itemType, tempOrderLineItemDto.PriceType);
            itemHeader.setText(tempOrderLineItemDto.ItemHeader);
            itemQty.setText("" + tempOrderLineItemDto.Quantity);
            itemAmount.setText("" + (tempOrderLineItemDto.Price * tempOrderLineItemDto.Quantity));

            demobtn.setId((int) tempOrderLineItemDto.ItemId);
            demobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "" + v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            linearLayout.addView(hiddenInfo);
        }
    }

    public void setImage(ImageView imgView, String key) {
        Bitmap bitmap = imageCache.getCacheFile(key);

        if (bitmap != null) {
            imgView.setImageBitmap(bitmap);
        }
    }

    public void setType(TextView txtView, int type) {
        switch (type) {
            case 1:
                txtView.setText("Quater Plate");
                break;
            case 2:
                txtView.setText("Half Plate");
                break;
            case 3:
                txtView.setText("Full Plate");
                break;
        }
    }
}

package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.support.annotation.IdRes;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDtoForOrder;
import com.google.gson.Gson;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderLineItemDto;

import java.util.ArrayList;

/**
 * Created by SAM on 6/4/2017.
 */

public class OrderFragment extends Fragment {

    ImageView itemImage;
    TextView itemHeader;
    TextView itemDescription;
    TextView quarterPrice;
    TextView halfPrice;
    TextView fullPrice;
    Button btnAddToCart;
    Button btnCheckOut;
    Button btnSave;
    LinearLayout layAddToCart;
    LinearLayout laySave;
    private RadioGroup radioGroup;
    private RadioButton radioButtonQuarterPrice;
    private RadioButton radioButtonHalfPrice;
    private RadioButton radioButtonFullPrice;
    Spinner stySpinner;
    TextView txtTotal;

    ItemDto item;
    ItemDtoForOrder itemDtoForOrder;
    OrderDto orderDto;
    OrderLineItemDto orderLineItemDto;

    TextStorage txtStorage;
    Alert alert;
    Gson gson;
    ImageDownloaderTask imageDownloaderTask;
    int itemIndex=-1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.order_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
        txtStorage = new TextStorage(getActivity());
        alert = new Alert(getActivity());
        gson = new Gson();

        itemImage = (ImageView) getActivity().findViewById(R.id.thumbImage);
        itemHeader = (TextView) getActivity().findViewById(R.id.txt_header);
        itemDescription = (TextView) getActivity().findViewById(R.id.txt_description);
        quarterPrice = (TextView) getActivity().findViewById(R.id.txt_quaterPrice);
        halfPrice = (TextView) getActivity().findViewById(R.id.txt_halfPrice);
        fullPrice = (TextView) getActivity().findViewById(R.id.txt_fullPrice);
        txtTotal = (TextView) getActivity().findViewById(R.id.txt_total);
        btnAddToCart = (Button) getActivity().findViewById(R.id.btn_addtocart);
        btnCheckOut = (Button) getActivity().findViewById(R.id.btn_placeorder);
        btnSave = (Button) getActivity().findViewById(R.id.btn_save);
        layAddToCart=(LinearLayout)getActivity().findViewById(R.id.layAddTOCart);
        laySave=(LinearLayout)getActivity().findViewById(R.id.laySave);
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
        radioButtonQuarterPrice = (RadioButton) getActivity().findViewById(R.id.radioQuaterPrice);
        radioButtonHalfPrice = (RadioButton) getActivity().findViewById(R.id.radioHalfPrice);
        radioButtonFullPrice = (RadioButton) getActivity().findViewById(R.id.radioFullPrice);
        stySpinner = (Spinner) getActivity().findViewById(R.id.spinner1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioQuaterPrice:
                        orderLineItemDto.PriceType = 1;
                        orderLineItemDto.Price = Integer.parseInt(quarterPrice.getText().toString());
                        break;
                    case R.id.radioHalfPrice:
                        orderLineItemDto.PriceType = 2;
                        orderLineItemDto.Price = Integer.parseInt(halfPrice.getText().toString());
                        break;
                    case R.id.radioFullPrice:
                        orderLineItemDto.PriceType = 3;
                        orderLineItemDto.Price = Integer.parseInt(fullPrice.getText().toString());
                        break;
                }
                UpdatePrice();
            }
        });

        stySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                orderLineItemDto.Quantity = Integer.parseInt(selected);
               /* Toast.makeText(getActivity(),
                        selected, Toast.LENGTH_SHORT).show();*/

                UpdatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.PriceType = 3;

        if (txtStorage.getEditItemId() == "") {

            if (getArguments() != null && getArguments().containsKey("ItemDto")) {
                itemDtoForOrder = (ItemDtoForOrder) getArguments().getSerializable("ItemDto");
                laySave.setVisibility(View.GONE);
            } else {
                FragmentManager fragmentManager = getFragmentManager();
                int count = fragmentManager.getBackStackEntryCount();
                if(count>0) {
                    fragmentManager.popBackStack();
                }
            }

        } else {
            itemDtoForOrder = GetItemForEidt();

        }

        item = new ItemDto();
        item.Itemid = itemDtoForOrder.Itemid;
        item.ItemDescription = itemDtoForOrder.ItemDescription;
        item.ItemHeader = itemDtoForOrder.ItemHeader;
        item.Categoryid = itemDtoForOrder.Categoryid;
        item.FullPrice = itemDtoForOrder.FullPrice;
        item.HalfPrice = itemDtoForOrder.HalfPrice;
        item.QuaterPrice = itemDtoForOrder.QuaterPrice;
        item.ImageUrl = itemDtoForOrder.ImageUrl;

        if(itemDtoForOrder.getImage()!=null) {
            itemImage.setImageBitmap(itemDtoForOrder.getImage());
        }else{
           imageDownloaderTask=new ImageDownloaderTask(itemImage);
            imageDownloaderTask.execute(item.ImageUrl);
        }
        itemHeader.setText(itemDtoForOrder.ItemHeader);
        itemDescription.setText(itemDtoForOrder.ItemDescription);
        quarterPrice.setText(Long.toString(itemDtoForOrder.QuaterPrice));
        halfPrice.setText(Long.toString(itemDtoForOrder.HalfPrice));
        fullPrice.setText(Long.toString(itemDtoForOrder.FullPrice));
        orderLineItemDto.Price = Integer.parseInt(fullPrice.getText().toString());

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart();
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart();
                Fragment fragment = new CartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveEditItem();
                Fragment fragment = new CartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });
        RefreshUI();
        UpdatePrice();
    }


    public Boolean ValidateOrder() {

        if (orderDto == null) {
            return false;
        }
        if (orderLineItemDto.Quantity < 1) {
            return false;
        }
        if (orderLineItemDto.ItemId < 1) {
            return false;
        }
        return true;
    }

    public void AddToCart() {
        orderLineItemDto.item = item;
        orderLineItemDto.ItemId = item.Itemid;
        orderLineItemDto.ImageUrl = item.ImageUrl;
        orderLineItemDto.ItemHeader = item.ItemHeader;

        //orderLineItemDto.Quantity=Integer.parseInt(quantityedt.getText().toString());

                /*int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButtonPrice = (RadioButton)getActivity().findViewById(selectedId);*/

               /* Toast.makeText(getActivity(),
                        radioButtonPrice.getText(), Toast.LENGTH_SHORT).show();*/

        String tempOrderDto = txtStorage.getCartData();
        if (tempOrderDto == "") {
            orderDto = new OrderDto();
        } else {
            orderDto = gson.fromJson(tempOrderDto, OrderDto.class);
        }

        if (orderDto.OrderLineItemList == null) {
            orderDto.OrderLineItemList = new ArrayList<OrderLineItemDto>();
        }


        if (ValidateOrder()) {
            orderDto.OrderLineItemList.add(orderLineItemDto);
        } else {
            return;
        }
        txtStorage.storeCartData(gson.toJson(orderDto));

        //Toast.makeText(getActivity(),txtStorage.getCartData(), Toast.LENGTH_SHORT).show();
    }

    public void UpdatePrice() {

        int tempTotal = 0;
        tempTotal = orderLineItemDto.Quantity * orderLineItemDto.Price;
        txtTotal.setText("" + tempTotal);
    }

    public ItemDtoForOrder GetItemForEidt() {
        String tempOrderDto = txtStorage.getCartData();
        String itemId = txtStorage.getEditItemId();
        itemIndex=Integer.parseInt(itemId);

        ItemDtoForOrder itemDtoForOrder = new ItemDtoForOrder();

        if (tempOrderDto == "") {
            orderDto = new OrderDto();
        } else {
            orderDto = gson.fromJson(tempOrderDto, OrderDto.class);

        }

        if (orderDto.OrderLineItemList == null) {
            orderDto.OrderLineItemList = new ArrayList<OrderLineItemDto>();
        }

        if (orderDto.OrderLineItemList.size() > 0) {

            OrderLineItemDto lineItemDto = orderDto.OrderLineItemList.get(Integer.parseInt(itemId));
            itemDtoForOrder.Itemid = lineItemDto.ItemId;
            itemDtoForOrder.ItemDescription = lineItemDto.item.ItemDescription;
            itemDtoForOrder.ItemHeader = lineItemDto.ItemHeader;
            itemDtoForOrder.Categoryid = lineItemDto.item.Categoryid;
            itemDtoForOrder.FullPrice = lineItemDto.item.FullPrice;
            itemDtoForOrder.HalfPrice = lineItemDto.item.HalfPrice;
            itemDtoForOrder.QuaterPrice = lineItemDto.item.QuaterPrice;
            itemDtoForOrder.ImageUrl = lineItemDto.ImageUrl;

            orderLineItemDto = lineItemDto;


            UpdatePrice();
        }
        txtStorage.storeEditItemId("");
        layAddToCart.setVisibility(View.GONE);
        return itemDtoForOrder;
    }

    public void RefreshUI(){

        if(orderLineItemDto.PriceType==1){
            radioButtonQuarterPrice.setChecked(true);
            radioButtonHalfPrice.setChecked(false);
            radioButtonFullPrice.setChecked(false);
        }
        else if(orderLineItemDto.PriceType==2){
            radioButtonQuarterPrice.setChecked(false);
            radioButtonHalfPrice.setChecked(true);
            radioButtonFullPrice.setChecked(false);
        }
        else if(orderLineItemDto.PriceType==3){
            radioButtonQuarterPrice.setChecked(false);
            radioButtonHalfPrice.setChecked(false);
            radioButtonFullPrice.setChecked(true);
        }
        if(orderLineItemDto.Quantity>1) {
            stySpinner.setSelection(orderLineItemDto.Quantity - 1);
        }
    }

    public void SaveEditItem() {
        orderLineItemDto.item = item;
        orderLineItemDto.ItemId = item.Itemid;
        orderLineItemDto.ImageUrl = item.ImageUrl;
        orderLineItemDto.ItemHeader = item.ItemHeader;

        String tempOrderDto = txtStorage.getCartData();
        if (tempOrderDto == "") {
            orderDto = new OrderDto();
        } else {
            orderDto = gson.fromJson(tempOrderDto, OrderDto.class);
        }

        if (orderDto.OrderLineItemList == null) {
            orderDto.OrderLineItemList = new ArrayList<OrderLineItemDto>();
        }

        orderDto.OrderLineItemList.set(itemIndex,orderLineItemDto);
        txtStorage.storeCartData(gson.toJson(orderDto));

    }

}

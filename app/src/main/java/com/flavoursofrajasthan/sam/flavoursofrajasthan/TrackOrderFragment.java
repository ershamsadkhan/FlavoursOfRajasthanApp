package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomProgress;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.TransaparentDialogue;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.ImageCache;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter.CustomListAdapter;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDtoForOrder;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderLineItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderSearch;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiClient;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAM on 6/4/2017.
 */

public class TrackOrderFragment extends Fragment {


    View rootView;
    ItemDto item;
    ItemDtoForOrder itemDtoForOrder;
    OrderLineItemDto orderLineItemDto;
    UserDto userDto;
    OrderSearch orderSearch;
    ApiRequest<OrderSearch> request;
    SimpleDateFormat dateFormat;

    TextStorage txtStorage;
    Gson gson;
    Alert alert;
    //CustomProgress customProgress;
    TransaparentDialogue tpg;
    ImageCache imageCache;

    Button btnBuy;

    LinearLayout fatherLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        if (container != null) {
            container.removeAllViews();
        }
        rootView = inflater.inflate(R.layout.content_track_order, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        //loadCartView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        try {
            alert = new Alert(getActivity());
            txtStorage = new TextStorage(getActivity());
            tpg=new TransaparentDialogue(getActivity());
            //customProgress = new CustomProgress(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));
            imageCache = ImageCache.getInstance();
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            orderSearch = new OrderSearch();
            orderSearch.UserId = Long.parseLong(txtStorage.getUserId());
            orderSearch.Type = 'N';
            orderSearch.FromDate = dateFormat.format(new Date());
            orderSearch.ToDate = dateFormat.format(new Date());

            request = new ApiRequest<OrderSearch>();
            request.Obj = orderSearch;

            GetNewOrders(request);

            FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
            if (floatingActionButton != null) {
                floatingActionButton.hide();
            }

            txtStorage = new TextStorage(getActivity());
            gson = new Gson();

        } catch (Exception ex) {
            alert.alertMessage("" + ex.getMessage());
        }
        getActivity().setTitle("Menu 1");
    }

    public void loadNewOrders(ArrayList<OrderDto> orderDtoList) {


        fatherLayout = (LinearLayout) rootView.findViewById(R.id.fatherlayout);
        for (OrderDto tempOrderDto : orderDtoList) {
            View hiddenMainInfo = getActivity().getLayoutInflater().inflate(R.layout.track_order_main_item, fatherLayout, false);
            LinearLayout linearLayoutMainCopy = (LinearLayout) hiddenMainInfo.findViewById(R.id.mainLayout);

            RelativeLayout relativeLayout = (RelativeLayout) linearLayoutMainCopy.findViewById(R.id.orderheaderrel);
            BeautifyOrderHeader(relativeLayout, tempOrderDto.GrandTotal, tempOrderDto.OrderNo,
                    linearLayoutMainCopy, tempOrderDto.OrderDate, tempOrderDto.OrderStatus);

            Button btnCancel = (Button) hiddenMainInfo.findViewById(R.id.btn_cancel);
            btnCancel.setId((int) tempOrderDto.OrderNo);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "" + v.getId(), Toast.LENGTH_SHORT).show();

                    ApiRequest<OrderDto> cancelOrderRequest=new ApiRequest<OrderDto>();
                    OrderDto tempCancelOrderDto=new OrderDto();
                    tempCancelOrderDto.OrderNo=v.getId();
                    cancelOrderRequest.Obj=tempCancelOrderDto;

                    CancelOrder(cancelOrderRequest );

                }
            });

            LinearLayout linearLayoutMainChildCopy = (LinearLayout) hiddenMainInfo.findViewById(R.id.mainchildLayout);
            for (int i = 0; i < tempOrderDto.OrderLineItemList.size(); i++) {

                OrderLineItemDto tempOrderLineItemDto = tempOrderDto.OrderLineItemList.get(i);
                View hiddenInfo = getActivity().getLayoutInflater().inflate(R.layout.track_order_item, linearLayoutMainCopy, false);

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


                linearLayoutMainChildCopy.addView(hiddenInfo);
            }
            fatherLayout.addView(hiddenMainInfo);
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

    public void GetNewOrders(ApiRequest<OrderSearch> request) {
        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<OrderDto>> call = apiService.getOrders(request);
        call.enqueue(new Callback<ApiResponse<OrderDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderDto>> call, Response<ApiResponse<OrderDto>> response) {
                //int statusCode = response.code();
                if (response.body() != null) {
                    if (response.body().Status == true) {
                        ArrayList<OrderDto> res = response.body().ObjList;
                        //alert.alertMessage(response.body().toString());

                        loadNewOrders(res);

                    } else {
                        alert.alertMessage(response.body().ErrMsg);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                    }
                } else {
                    alert.alertMessage("" + "server error");
                }
                tpg.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage("" + getString(R.string.server_error));
                tpg.dismiss();
                Log.e("Api Failure", t.toString());
            }
        });
    }

    public void CancelOrder(ApiRequest<OrderDto> request) {
        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<OrderDto>> call = apiService.cancelOrder(request);
        call.enqueue(new Callback<ApiResponse<OrderDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderDto>> call, Response<ApiResponse<OrderDto>> response) {
                //int statusCode = response.code();
                if (response.body() != null) {
                    if (response.body().Status == true) {

                        Fragment fragment = new TrackOrderFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, fragment)
                                .commit();
                        //alert.alertMessage(response.body().toString());

                    } else {
                        alert.alertMessage(response.body().ErrMsg);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                    }
                } else {
                    alert.alertMessage("Server Error");
                }
                tpg.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage("" + getString(R.string.server_error));
                tpg.dismiss();
                Log.e("Api Failure", t.toString());
            }
        });
    }

    public void BeautifyOrderHeader(RelativeLayout relativeLayout, Long total, Long OrderNo,
                                    LinearLayout linearLayoutMainCopy, String Date, String OrderStatus) {
        TextView orderNo = (TextView) relativeLayout.findViewById(R.id.orderno);
        TextView orderStatus = (TextView) relativeLayout.findViewById(R.id.orderstatus);
        Button cancelBtn=(Button) linearLayoutMainCopy.findViewById(R.id.btn_cancel);
        orderNo.setText("#Order No: " + OrderNo);

        TextView tvGrandTotal = (TextView) linearLayoutMainCopy.findViewById(R.id.totalamount);
        tvGrandTotal.setText("" + total);


        switch (OrderStatus) {
            case "P":
                orderStatus.setText("Placed");
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.placedOrder));
                break;
            case "C":
                orderStatus.setText("Cancelled");
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.cancelledOrder));
                cancelBtn.setVisibility(View.INVISIBLE);
                break;
            case "O":
                orderStatus.setText("Out For Delivery");
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.outForDeliveryOrder));
                cancelBtn.setVisibility(View.INVISIBLE);
                break;
            case "D":
                orderStatus.setText("Delivered");
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.deliveredOrder));
                cancelBtn.setVisibility(View.INVISIBLE);
                break;
        }

    }
}

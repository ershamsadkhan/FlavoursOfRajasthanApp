package com.flavoursofrajasthan.sam.flavoursofrajasthan;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomProgress;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.TransaparentDialogue;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDtoForOrder;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderLineItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiClient;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAM on 6/4/2017.
 */

public class ConfirmOrderFragment extends Fragment {

    TextView userName;
    TextView userEmail;
    EditText deliveryAddress;
    TextView grandTotal;
    Spinner citySpinner;
    Button btnConfirm;

    TextStorage txtStorage;
    Gson gson;

    ItemDto item;
    ItemDtoForOrder itemDtoForOrder;
    OrderDto orderDto;
    OrderLineItemDto orderLineItemDto;
    UserDto userDto;

    Alert alert;
    ApiRequest<UserDto> apiRequest;
    ApiRequest<OrderDto> apiOrderRequest;
    //CustomProgress customProgress;
    TransaparentDialogue tpg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.confirm_order, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        alert = new Alert(getActivity());
        tpg=new TransaparentDialogue(getActivity());
        //customProgress = new CustomProgress(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));
        txtStorage = new TextStorage(getActivity());
        gson = new Gson();

        apiRequest = new ApiRequest<UserDto>();
        userDto = new UserDto();
        String UserId = txtStorage.getUserId();
        userDto.Userid = Long.parseLong(UserId);
        userDto.UserName = txtStorage.getUserName();
        userDto.UserPwd = txtStorage.getUserPwd();
        apiRequest.Obj = userDto;
        if (userDto.Userid > 0 && userDto.UserName != "" && userDto.UserPwd != "") {
            LogInUser();
        }

        btnConfirm = (Button) getActivity().findViewById(R.id.btn_buy);
        String tempOrderDto = txtStorage.getCartData();
        if (tempOrderDto == "") {
            alert.alertMessage("No Items present in the cart");
        } else {
            orderDto = gson.fromJson(tempOrderDto, OrderDto.class);
        }

        orderDto.CityCode=1;
        deliveryAddress = (EditText) getActivity().findViewById(R.id.input_address);
        citySpinner = (Spinner) getActivity().findViewById(R.id.spinnercity);
        userName = (TextView) getActivity().findViewById(R.id.userName);
        userEmail = (TextView) getActivity().findViewById(R.id.userEmail);
        grandTotal = (TextView) getActivity().findViewById(R.id.grandtotal);
        grandTotal.setText(""+orderDto.getGrandTotal());

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String selectedVal = getResources().getStringArray(R.array.cities_array_values)[citySpinner.getSelectedItemPosition()];

                orderDto.CityCode = Integer.parseInt(selectedVal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alert.alertMessage("Order Place");
                PlaceOrder();
            }
        });

        getActivity().setTitle("Menu 1");
    }

    public void LogInUser() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<UserDto>> call = apiService.logIn(apiRequest);
        call.enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call, Response<ApiResponse<UserDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                } else {
                    userDto = response.body().ObjList.get(0);
                    userName.setText(userDto.UserName);
                    userEmail.setText(userDto.UserEmailAddress);
                    deliveryAddress.setText(userDto.PrimaryAddress);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage("" + getString(R.string.server_error));
                Log.e("Api Failure", t.toString());
            }
        });
    }

    public void PlaceOrder(){
        orderDto.UserId=Long.parseLong(txtStorage.getUserId());
        orderDto.DeliveryAddress=deliveryAddress.getText().toString();
        apiOrderRequest = new ApiRequest<OrderDto>();
        apiOrderRequest.Obj=orderDto;

        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<OrderDto>> call = apiService.placeOrder(apiOrderRequest);
        call.enqueue(new Callback<ApiResponse<OrderDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderDto>> call, Response<ApiResponse<OrderDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                } else {
                    alert.alertMessage("Order Placed Successfully");
                    Fragment fragment = new TrackOrderFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
                    txtStorage.storeCartData("");
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
}

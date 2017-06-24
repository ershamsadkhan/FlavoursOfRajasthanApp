package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomProgress;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomToast;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.TransaparentDialogue;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Offer.OfferDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderLineItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiClient;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAM on 6/18/2017.
 */

public class OffersFragment extends Fragment {

    FragmentManager fragmentManager;
    CustomProgress customProgress;
    LinearLayout linearLayout;
    View rootView;

    Alert alert;
    CustomToast customToast;

    TransaparentDialogue tpg;

    ArrayList<OfferDto> offerDtoArrayList;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        tpg=new TransaparentDialogue(getActivity());
        alert = new Alert(getActivity());
        customToast = new CustomToast(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        rootView = inflater.inflate(R.layout.offers_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.mainlayout);

        GetOffers();
    }

    public void GetOffers(){
        tpg.show();

        ApiRequest<OfferDto> request=new ApiRequest<OfferDto>();
        OfferDto offerDto=new OfferDto();
        request.Obj=offerDto;

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<OfferDto>> call = apiService.GetOffers(request);
        call.enqueue(new Callback<ApiResponse<OfferDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<OfferDto>> call, Response<ApiResponse<OfferDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                } else {
                    //alert.alertMessage(response.body().ObjList.toString());
                    offerDtoArrayList=response.body().ObjList;
                    LoadOffers();
                }
                tpg.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<OfferDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage("" + getString(R.string.server_error));
                Log.e("Api Failure", t.toString());
                tpg.dismiss();
            }
        });
    }

    public void LoadOffers(){
        for (int i = 0; i < offerDtoArrayList.size(); i++) {
            OfferDto tempOfferDto = offerDtoArrayList.get(i);
            View hiddenInfo = getActivity().getLayoutInflater().inflate(R.layout.offers_item, linearLayout, false);
            TextView itemHeader = (TextView) hiddenInfo.findViewById(R.id.item_header);
            TextView itemDescription = (TextView) hiddenInfo.findViewById(R.id.item_description);

            itemHeader.setText(tempOfferDto.OfferHeader);
            itemDescription.setText("" + tempOfferDto.OfferDescription);

            linearLayout.addView(hiddenInfo);
        }
    }
}

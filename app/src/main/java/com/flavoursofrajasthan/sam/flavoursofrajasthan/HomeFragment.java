package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.graphics.Movie;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomProgress;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.TransaparentDialogue;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter.CustomListAdapter;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiClient;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAM on 6/4/2017.
 */

public class HomeFragment extends Fragment {


    public CustomListAdapter sta;
    Alert alert;
    CustomProgress customProgress;
    Button btnItemClick;

    FloatingActionButton floatingActionButton;
    FragmentManager fragmentManager;
    ListView listView;
    int retry=0;

    TransaparentDialogue tpg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.content_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Menu 1");

        tpg=new TransaparentDialogue(getActivity());
        fragmentManager= getActivity().getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fragmentManager.popBackStack();
        }

        listView= (ListView) getView().findViewById(R.id.items_list);
        btnItemClick = (Button) getView().findViewById(R.id.btn_item_selected);

        alert = new Alert(getActivity());
        customProgress = new CustomProgress(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));

        floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                /*ItemDto newsData = (ItemDto) listView.getItemAtPosition(position);

                Fragment fragment = new OrderFragment();

                fragmentManager.beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                        .addToBackStack("tag")
                        .commit();*/

                //Toast.makeText(getActivity(), "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mLastFirstVisibleItem < firstVisibleItem) {
                    if (floatingActionButton != null) {
                        floatingActionButton.hide();
                    }
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    if (floatingActionButton != null) {
                        floatingActionButton.show();
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });

        getListData();

    }

    public void getListData(){
        //customProgress.show();
        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<CategoryDto>> call = apiService.getItems(new ApiRequest<CategoryDto>());
        call.enqueue(new Callback<ApiResponse<CategoryDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryDto>> call, Response<ApiResponse<CategoryDto>> response) {
                //int statusCode = response.code();
                if(response.body()!=null) {
                    if (response.body().Status == true) {
                        ArrayList<CategoryDto> res = response.body().ObjList;
                        sta = new CustomListAdapter(getActivity(), res.get(0).itemDtoList);
                        listView.setAdapter(sta);


                        for (ItemDto s : res.get(0).itemDtoList) {
                            //START LOADING IMAGES FOR EACH STUDENT
                            s.loadImage(sta);
                        }
                        tpg.dismiss();
                    } else {
                        alert.alertMessage(response.body().ErrMsg);
                    }
                }else{
                    alert.alertMessage("" + "server error");
                    tpg.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CategoryDto>> call, Throwable t) {
                // Log error here since request failed
                if(retry<3){
                    tpg.dismiss();
                    getListData();
                    retry++;
                }
                else {
                    tpg.dismiss();
                    alert.alertMessage("" + getString(R.string.server_error));
                }
                Log.e("Api Failure", t.toString());
            }
        });
    }

}

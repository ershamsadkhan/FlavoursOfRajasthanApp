package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.content_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Menu 1");

        //ArrayList<ItemDto> listData = getListData();

        final ListView listView = (ListView) getView().findViewById(R.id.items_list);



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<CategoryDto>> call = apiService.getItems(new ApiRequest<CategoryDto>());
        call.enqueue(new Callback<ApiResponse<CategoryDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryDto>> call, Response<ApiResponse<CategoryDto>> response) {
                //int statusCode = response.code();
                ArrayList<CategoryDto> res= response.body().ObjList;
                sta = new CustomListAdapter(getActivity(), res.get(0).itemDtoList);
                listView.setAdapter(sta);

                for (ItemDto s : res.get(0).itemDtoList) {
                     //START LOADING IMAGES FOR EACH STUDENT
                    s.loadImage(sta);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CategoryDto>> call, Throwable t) {
                // Log error here since request failed
                Log.e("APi Failure", t.toString());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                ItemDto newsData = (ItemDto) listView.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<ItemDto> getListData() {
        ArrayList<ItemDto> listMockData = new ArrayList<ItemDto>();
        String[] images = getResources().getStringArray(R.array.images_array);
        String[] headlines = getResources().getStringArray(R.array.headline_array);

        for (int i = 0; i < images.length; i++) {
            ItemDto newsData = new ItemDto();
            newsData.setUrl(images[i]);
            newsData.setHeadline(headlines[i]);
            newsData.setReporterName("Pankaj Gupta");
            newsData.setDate("May 26, 2013, 13:35");
            listMockData.add(newsData);
        }
        return listMockData;
    }
}

package com.flavoursofrajasthan.sam.flavoursofrajasthan.rest;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiInterface {
    @POST("api/Item/GetItems")
    Call<ApiResponse<CategoryDto>> getItems(@Body ApiRequest<CategoryDto> request);

}

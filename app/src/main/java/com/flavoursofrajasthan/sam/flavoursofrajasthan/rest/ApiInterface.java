package com.flavoursofrajasthan.sam.flavoursofrajasthan.rest;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiInterface {
    @POST("api/Item/GetItems")
    Call<ApiResponse<CategoryDto>> getItems(@Body ApiRequest<CategoryDto> request);

    @POST("api/User/SignUp")
    Call<ApiResponse<UserDto>> signUp(@Body ApiRequest<UserDto> request);

    @POST("api/User/LogIn")
    Call<ApiResponse<UserDto>> logIn(@Body ApiRequest<UserDto> request);

    @POST("api/User/UpdateDetails")
    Call<ApiResponse<UserDto>> UpdateUser(@Body ApiRequest<UserDto> request);

}

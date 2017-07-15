package com.flavoursofrajasthan.sam.flavoursofrajasthan.rest;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Offer.OfferDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order.OrderSearch;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {
    @POST("api/Item/GetItems")
    Call<ApiResponse<CategoryDto>> getItems(@Body ApiRequest<CategoryDto> request);

    @POST("api/Order/PlaceOrder")
    Call<ApiResponse<OrderDto>> placeOrder(@Body ApiRequest<OrderDto> request);

    @POST("api/Order/GetOrders")
    Call<ApiResponse<OrderDto>> getOrders(@Body ApiRequest<OrderSearch> request);

    @POST("api/Order/CancelOrder")
    Call<ApiResponse<OrderDto>> cancelOrder(@Body ApiRequest<OrderDto> request);

    @POST("api/User/SignUp")
    Call<ApiResponse<UserDto>> signUp(@Body ApiRequest<UserDto> request);

    @POST("api/User/LogInForUsers")
    Call<ApiResponse<UserDto>> logIn(@Body ApiRequest<UserDto> request);

    @POST("api/User/UpdateDetails")
    Call<ApiResponse<UserDto>> UpdateUser(@Body ApiRequest<UserDto> request);

    @POST("api/Offer/GetOffers")
    Call<ApiResponse<OfferDto>> GetOffers(@Body ApiRequest<OfferDto> request);

    @POST("api/Offer/Applicableoffers")
    Call<ApiResponse<OfferDto>> ApplicableOffers(@Body ApiRequest<OfferDto> request);

    @POST("api/User/ForgotPassword")
    Call<ApiResponse<UserDto>> ForgorPassword(@Query("UserName") String UserName);

}

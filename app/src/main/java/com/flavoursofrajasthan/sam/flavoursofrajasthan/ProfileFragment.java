package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.content.DialogInterface;
import android.graphics.Movie;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomToast;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter.CustomListAdapter;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.CategoryDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiRequest;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.ApiResponse;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User.UserDto;
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

public class ProfileFragment extends Fragment {
    Button saveBtn;
    EditText userNameEditText;
    EditText userPwdEditText;
    EditText primaryAddressEditText;
    EditText emailAddressEditText;
    EditText phoneNumberEditText;

    TextStorage txtStorage;
    UserDto userDto;
    ApiRequest<UserDto> apiRequest;
    Alert alert;
    CustomToast customToast;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        alert=new Alert(getActivity());
        txtStorage = new TextStorage(getActivity());
        customToast=new CustomToast(getActivity(),getActivity(),getLayoutInflater(savedInstanceState));

        saveBtn = (Button) getActivity().findViewById(R.id.btn_save);
        userNameEditText = (EditText) getActivity().findViewById((R.id.input_name));
        userPwdEditText = (EditText) getActivity().findViewById((R.id.input_password));
        primaryAddressEditText = (EditText) getActivity().findViewById((R.id.input_primary_address));
        emailAddressEditText = (EditText) getActivity().findViewById((R.id.input_email));
        phoneNumberEditText = (EditText) getActivity().findViewById((R.id.input_phone));

        apiRequest = new ApiRequest<UserDto>();
        userDto = new UserDto();
        apiRequest.Obj = userDto;

        String UserId = txtStorage.getUserId();
        userDto.Userid = Long.parseLong(UserId);
        userDto.UserName = txtStorage.getUserName();
        userDto.UserPwd = txtStorage.getUserPwd();

        if (userDto.Userid > 0 && userDto.UserName != "" && userDto.UserPwd != "") {

        } else {

        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "btnCLicked", Toast.LENGTH_SHORT).show();
                userDto.UserName = userNameEditText.getText().toString();
                userDto.UserPwd = userPwdEditText.getText().toString();
                userDto.PrimaryAddress = primaryAddressEditText.getText().toString();
                userDto.UserEmailAddress = emailAddressEditText.getText().toString();
                userDto.UserPhoneNumber = phoneNumberEditText.getText().toString();
                SignUpUser();
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SignUpUser() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<UserDto>> call = apiService.signUp(apiRequest);
        call.enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call, Response<ApiResponse<UserDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                }
                else{
                    customToast.show("Successfully Signed Up.");
                    Fragment fragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage(""+getString(R.string.server_error));
                Log.e("Api Failure", t.toString());
            }
        });
    }

}

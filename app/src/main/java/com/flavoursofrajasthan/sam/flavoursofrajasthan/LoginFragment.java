package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.content.DialogInterface;
import android.graphics.Movie;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.CustomToast;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.TransaparentDialogue;
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

public class LoginFragment extends Fragment {
    Button saveBtn;
    EditText userNameEditText;
    EditText userPwdEditText;
    TextView forgotPwd;
    TextView createAccount;

    TextStorage txtStorage;
    UserDto userDto;
    ApiRequest<UserDto> apiRequest;
    Alert alert;
    CustomToast customToast;

    TransaparentDialogue tpg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        if (container != null) {
            container.removeAllViews();
        }
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.login, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        tpg=new TransaparentDialogue(getActivity());
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        alert = new Alert(getActivity());
        txtStorage = new TextStorage(getActivity());
        customToast = new CustomToast(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));

        saveBtn = (Button) getActivity().findViewById(R.id.btn_login);
        userNameEditText = (EditText) getActivity().findViewById((R.id.input_name));
        userPwdEditText = (EditText) getActivity().findViewById((R.id.input_password));
        forgotPwd=(TextView)getActivity().findViewById((R.id.link_forgotpwd));
        createAccount=(TextView)getActivity().findViewById((R.id.link_signup));

        apiRequest = new ApiRequest<UserDto>();
        userDto = new UserDto();
        apiRequest.Obj = userDto;

        String UserId = txtStorage.getUserId();
        userDto.Userid = Long.parseLong(UserId);
        userDto.UserName = txtStorage.getUserName();
        userDto.UserPwd = txtStorage.getUserPwd();

        if (userDto.Userid > 0 && userDto.UserName != "" && userDto.UserPwd != "") {
            Fragment fragment = new ProfileFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .commit();
        } else {

        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDto.UserName = userNameEditText.getText().toString();
                userDto.UserPwd = userPwdEditText.getText().toString();

                LogInUser();
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
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

    public void LogInUser() {
        tpg.show();
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
                    customToast.show("Successfully Logged In.");

                    String temp=Long.toString(response.body().ObjList.get(0).Userid);
                    txtStorage.storeUserId(Long.toString(response.body().ObjList.get(0).Userid));
                    txtStorage.storeUserName(response.body().ObjList.get(0).UserName);
                    txtStorage.storeUserPwd(response.body().ObjList.get(0).UserPwd);

                    Fragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack();
                    /*fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .addToBackStack("tag")
                            .commit();*/
                }
                tpg.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserDto>> call, Throwable t) {
                // Log error here since request failed
                alert.alertMessage("" + getString(R.string.server_error));
                Log.e("Api Failure", t.toString());
                tpg.dismiss();
            }
        });
    }

}

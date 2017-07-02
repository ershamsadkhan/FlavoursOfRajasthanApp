package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.content.DialogInterface;
import android.graphics.Movie;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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

public class ProfileFragment extends Fragment {
    Button saveBtn;
    Button registerBtn;
    Button logoutBtn;
    EditText userNameEditText;
    EditText userPwdEditText;
    EditText userConfirmPwdEditText;
    EditText primaryAddressEditText;
    EditText emailAddressEditText;
    EditText phoneNumberEditText;
    TextInputLayout txtInputLayout;
    TextInputLayout txtConfirmInputLayout;

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
        return inflater.inflate(R.layout.profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        tpg = new TransaparentDialogue(getActivity());
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        TextView title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title.setText("PROFILE");
        title.setTypeface(null);
        title.setTextSize(20);

        alert = new Alert(getActivity());
        txtStorage = new TextStorage(getActivity());
        customToast = new CustomToast(getActivity(), getActivity(), getLayoutInflater(savedInstanceState));

        saveBtn = (Button) getActivity().findViewById(R.id.btn_save);
        registerBtn = (Button) getActivity().findViewById(R.id.btn_register);
        logoutBtn = (Button) getActivity().findViewById(R.id.btn_logout);
        txtInputLayout = (TextInputLayout) getActivity().findViewById(R.id.showPwd);
        txtConfirmInputLayout = (TextInputLayout) getActivity().findViewById(R.id.showConfirmPwd);

        userNameEditText = (EditText) getActivity().findViewById((R.id.input_name));
        userPwdEditText = (EditText) getActivity().findViewById((R.id.input_password));
        userConfirmPwdEditText = (EditText) getActivity().findViewById((R.id.input_confirm_password));
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

        //if user is already present then fetch details and show it on the screen
        //else ask him to sign up
        if (userDto.Userid > 0 && userDto.UserName != "" && userDto.UserPwd != "") {
            saveBtn.setVisibility(View.GONE);
            registerBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            userPwdEditText.setVisibility(View.GONE);
            txtInputLayout.setVisibility(View.GONE);
            txtConfirmInputLayout.setVisibility(View.GONE);
            disableFields();
            LogInUser();
        } else {
            saveBtn.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.GONE);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDto.UserName = userNameEditText.getText().toString();
                userDto.UserPwd = userPwdEditText.getText().toString();
                userDto.PrimaryAddress = primaryAddressEditText.getText().toString();
                userDto.UserEmailAddress = emailAddressEditText.getText().toString();
                userDto.UserPhoneNumber = phoneNumberEditText.getText().toString();
                if (ValidateFields()) {
                    SignUpUser();
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customToast.show("Successfully Logged Out!");
                txtStorage.storeUserId("0");
                txtStorage.storeUserPwd("");
                txtStorage.storeUserName("");
                Fragment fragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                //fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .commit();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDto.UserName = userNameEditText.getText().toString();
                userDto.UserPwd = userPwdEditText.getText().toString();
                userDto.PrimaryAddress = primaryAddressEditText.getText().toString();
                userDto.UserEmailAddress = emailAddressEditText.getText().toString();
                userDto.UserPhoneNumber = phoneNumberEditText.getText().toString();
                apiRequest.Obj = userDto;
                if (ValidateFields()) {
                    UpdateUser();
                }
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
            if (saveBtn.getVisibility() == View.GONE) {
                enableFields();
                saveBtn.setVisibility(View.GONE);
                registerBtn.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void UpdateUser() {
        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<UserDto>> call = apiService.UpdateUser(apiRequest);
        call.enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call, Response<ApiResponse<UserDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                } else {
                    customToast.show("Successfully Updated.");
                    saveBtn.setVisibility(View.GONE);
                    registerBtn.setVisibility(View.GONE);
                    logoutBtn.setVisibility(View.VISIBLE);
                    disableFields();
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
                    userDto = response.body().ObjList.get(0);

                    userNameEditText.setText(userDto.UserName);
                    userPwdEditText.setText(userDto.UserPwd);
                    primaryAddressEditText.setText(userDto.PrimaryAddress);
                    emailAddressEditText.setText(userDto.UserEmailAddress);
                    phoneNumberEditText.setText(userDto.UserPhoneNumber);
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

    public void SignUpUser() {
        tpg.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiResponse<UserDto>> call = apiService.signUp(apiRequest);
        call.enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call, Response<ApiResponse<UserDto>> response) {
                //int statusCode = response.code();
                if (response.body().Status == false) {
                    alert.alertMessage(response.body().ErrMsg);
                } else {
                    customToast.show("Successfully Signed Up.");
                    Fragment fragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
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

    public void enableFields() {
        userNameEditText.setEnabled(false);
        userPwdEditText.setEnabled(true);
        primaryAddressEditText.setEnabled(true);
        emailAddressEditText.setEnabled(true);
        phoneNumberEditText.setEnabled(true);
    }

    public void disableFields() {
        userNameEditText.setEnabled(false);
        userPwdEditText.setEnabled(false);
        primaryAddressEditText.setEnabled(false);
        emailAddressEditText.setEnabled(false);
        phoneNumberEditText.setEnabled(false);
    }

    public Boolean ValidateFields() {
        Boolean result = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (userNameEditText.getText().toString().trim().length() == 0) {
            userNameEditText.requestFocus();
            userNameEditText.setError(
                    "UserName Is Required"
            );
            result = false;
        } else if (phoneNumberEditText.getText().toString().trim().length() == 0) {
            phoneNumberEditText.requestFocus();
            phoneNumberEditText.setError(
                    "Phone No Is Required"
            );
            result = false;
        }
        else if (phoneNumberEditText.getText().toString().trim().length() <10) {
            phoneNumberEditText.requestFocus();
            phoneNumberEditText.setError(
                    "Invalid Phone Number"
            );
            result = false;
        }
        else if (emailAddressEditText.getText().toString().trim().length() == 0) {
            emailAddressEditText.requestFocus();
            emailAddressEditText.setError(
                    "Email Id Is Required"
            );
            result = false;
        }
        else if (!emailAddressEditText.getText().toString().trim().matches(emailPattern)) {
            emailAddressEditText.requestFocus();
            emailAddressEditText.setError(
                    "Invalid Email Id"
            );
            result = false;
        }else if (primaryAddressEditText.getText().toString().trim().length() == 0) {
            primaryAddressEditText.requestFocus();
            primaryAddressEditText.setError(
                    "Address Is Required"
            );
            result = false;
        } else if (userPwdEditText.getVisibility()==View.VISIBLE && userPwdEditText.getText().toString().trim().length() == 0) {
            userPwdEditText.requestFocus();
            userPwdEditText.setError(
                    "Password Is Required"
            );
            result = false;
        }
        else if (userPwdEditText.getVisibility()==View.VISIBLE && userPwdEditText.getText().toString().trim().length() <6) {
            userPwdEditText.requestFocus();
            userPwdEditText.setError(
                    "Alteast 6 Characters Is Required"
            );
            result = false;
        }
        else if (userPwdEditText.getVisibility()==View.VISIBLE && !userPwdEditText.getText().toString().equals(userConfirmPwdEditText.getText().toString().trim())) {
            userConfirmPwdEditText.requestFocus();
            userConfirmPwdEditText.setError(
                    "Please check your password!"
            );
            result = false;
        }else {
            result = true;
        }
        return result;
    }
}

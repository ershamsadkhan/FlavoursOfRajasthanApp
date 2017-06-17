package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by SAM on 6/4/2017.
 */

public class ConfirmOrderFragment extends Fragment {

    EditText deliveryAddress;
    Spinner citySpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.confirm_order, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        deliveryAddress = (EditText) getActivity().findViewById(R.id.input_address);
        citySpinner=(Spinner)getActivity().findViewById(R.id.spinnercity) ;

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String selectedVal = getResources().getStringArray(R.array.cities_array_values)[citySpinner.getSelectedItemPosition()];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getActivity().setTitle("Menu 1");
    }
}

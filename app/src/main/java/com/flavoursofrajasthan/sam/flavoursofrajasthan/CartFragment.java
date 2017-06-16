package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by SAM on 6/4/2017.
 */

public class CartFragment extends Fragment {

    LinearLayout linearLayout;
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.cart_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.mainLayout);
        loadCartView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        getActivity().setTitle("Menu 1");
    }

    public void loadCartView(){
        for(int i=0;i<3;i++) {
            View hiddenInfo = getActivity().getLayoutInflater().inflate(R.layout.cart_item, linearLayout, false);
            Button demobtn = (Button) hiddenInfo.findViewById(R.id.btn_edit);
            demobtn.setId(i);
            demobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "" + v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            linearLayout.addView(hiddenInfo);
        }
    }
}

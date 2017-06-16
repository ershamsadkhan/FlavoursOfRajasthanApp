package com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.R;

/**
 * Created by SAM on 6/6/2017.
 */

public class CustomToast {
    public Context context;
    LayoutInflater inflater;
    Activity activity;
    public CustomToast(Context context, Activity activity,LayoutInflater inflater){
        this.context=context;
        this.inflater=inflater;
        this.activity=activity;
    }

    public void show(String message) {
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) activity.findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}

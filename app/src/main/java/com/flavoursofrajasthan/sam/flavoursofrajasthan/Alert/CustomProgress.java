package com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.R;

/**
 * Created by SAM on 6/6/2017.
 */

public class CustomProgress {

    public Context context;
    LayoutInflater inflater;
    Activity activity;
    ProgressDialog pd;
    public CustomProgress(Context context, Activity activity,LayoutInflater inflater){
        this.context=context;
        this.inflater=inflater;
        this.activity=activity;
    }

    public void show() {
        //LayoutInflater inflater = this.inflater;
        //ProgressBar bar = (ProgressBar ) this.inflater.inflate(R.layout.small_progress_bar, null);
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        //pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        //pd.getWindow().setGravity(Gravity.CENTER);

        pd.show();
    }

    public void dismiss() {
        pd.dismiss();
    }
}

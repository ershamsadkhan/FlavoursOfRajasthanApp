package com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by SAM on 6/6/2017.
 */

public class Alert {

    public Context context;

    public Alert(Context context){
        this.context=context;
    }

    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("OK", dialogClickListener).show();
    }
}

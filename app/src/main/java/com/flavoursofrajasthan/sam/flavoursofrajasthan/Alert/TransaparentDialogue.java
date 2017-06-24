package com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.R;

public class TransaparentDialogue extends Dialog {

    private ImageView iv;

    public TransaparentDialogue(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //iv = new ImageView(context);
        //iv.setImageResource(resourceIdOfImage);
        ProgressBar pb=new ProgressBar(context);
        pb.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(android.R.color.holo_orange_dark),
                android.graphics.PorterDuff.Mode.MULTIPLY);


        layout.addView(pb, params);
        addContentView(layout, params);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);
        //iv.setAnimation(anim);
        //iv.startAnimation(anim);
    }
}
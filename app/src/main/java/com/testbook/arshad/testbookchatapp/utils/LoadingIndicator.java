package com.testbook.arshad.testbookchatapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.testbook.arshad.testbookchatapp.R;

/**
 * Created by arshad on 05/10/18.
 */

public class LoadingIndicator {

    private String message;
    private Activity activity;
    private boolean cancel;
    private static Dialog dialog;

    public LoadingIndicator(Builder builder){
        this.message=builder.message;
        this.activity=builder.activity;
        this.cancel=builder.cancel;
    }


    public static class Builder{
        private String message;
        private Activity activity;
        private boolean cancel;

        public Builder(Activity activity){
            this.activity=activity;
        }

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder isCancellable(boolean cancel){
            this.cancel=cancel;
            return this;
        }

        public LoadingIndicator build(){
            TextView message1;

            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }

            dialog=new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.custom_loading);


            //getting resources
            message1= dialog.findViewById(R.id.message);

            if(message!=null && message.length()>0) {
                message1.setText(message);
                message1.setVisibility(View.VISIBLE);
            }else {
                message1.setText("");
                message1.setVisibility(View.GONE);
            }
            dialog.show();

            return new LoadingIndicator(this);
        }
    }

    public void hideLoadingIndicator(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}


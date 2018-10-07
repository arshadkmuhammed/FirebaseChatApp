package com.testbook.arshad.testbookchatapp.utils.dialogue;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.testbook.arshad.testbookchatapp.R;


/**
 * Created by arshad on 05/10/18.
 */

public class CustomDialogue {

    private String title;
    private String message;
    private String positiveBtnText;
    private String negativeBtnText;
    private String pBtnColor;
    private String nBtnColor;
    private Activity activity;
    private CustomDialogueClickListener pListener,nListener;
    private boolean cancel;
    int gifImageResource;
    private static Dialog dialog;

    private CustomDialogue(Builder builder){
        this.title=builder.title;
        this.message=builder.message;
        this.activity=builder.activity;
        this.pListener=builder.pListener;
        this.nListener=builder.nListener;
        this.pBtnColor=builder.pBtnColor;
        this.nBtnColor=builder.nBtnColor;
        this.positiveBtnText=builder.positiveBtnText;
        this.negativeBtnText=builder.negativeBtnText;
        this.gifImageResource=builder.gifImageResource;
        this.cancel=builder.cancel;
    }


    public static class Builder{
        private String title,message,positiveBtnText,negativeBtnText,pBtnColor,nBtnColor;
        private Activity activity;
        private CustomDialogueClickListener pListener,nListener;
        private boolean cancel;
        int gifImageResource;

        public Builder(Activity activity){
            this.activity=activity;
        }
        public Builder setTitle(String title){
            this.title=title;
            return this;
        }


        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder setPositiveBtnText(String positiveBtnText){
            this.positiveBtnText=positiveBtnText;
            return this;
        }

        public Builder setPositiveBtnBackground(String pBtnColor){
            this.pBtnColor=pBtnColor;
            return this;
        }


        public Builder setNegativeBtnText(String negativeBtnText){
            this.negativeBtnText=negativeBtnText;
            return this;
        }

        public Builder setNegativeBtnBackground(String nBtnColor){
            this.nBtnColor=nBtnColor;
            return this;
        }

        //set Positive listener
        public Builder OnPositiveClicked(CustomDialogueClickListener pListener){
            this.pListener=pListener;
            return this;
        }

        //set Negative listener
        public Builder OnNegativeClicked(CustomDialogueClickListener nListener){
            this.nListener=nListener;
            return this;
        }

        public Builder isCancellable(boolean cancel){
            this.cancel=cancel;
            return this;
        }
        public Builder setGifResource(int gifImageResource){
            this.gifImageResource=gifImageResource;
            return this;
        }

        public CustomDialogue build(){
            TextView message1,title1;
            ImageView iconImg;
            Button nBtn,pBtn;
            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }
            dialog=new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.custom_dialog);


            //getting resources
            title1= dialog.findViewById(R.id.title);
            message1= dialog.findViewById(R.id.message);
            nBtn= dialog.findViewById(R.id.negativeBtn);
            pBtn= dialog.findViewById(R.id.positiveBtn);

            if(title!=null && title.length()>0) {
                title1.setText(title);
                title1.setVisibility(View.VISIBLE);
            }else {
                title1.setText("");
                title1.setVisibility(View.GONE);
            }

            if(message!=null && message.length()>0) {
                message1.setText(message);
                message1.setVisibility(View.VISIBLE);
            }else {
                message1.setText("");
                message1.setVisibility(View.GONE);
            }

            if(positiveBtnText!=null && positiveBtnText.length()>0) {
                pBtn.setText(positiveBtnText);
                pBtn.setVisibility(View.VISIBLE);
            }else {
                pBtn.setText("");
                pBtn.setVisibility(View.GONE);
            }

            if(negativeBtnText!=null && negativeBtnText.length()>0) {
                nBtn.setText(negativeBtnText);
                nBtn.setVisibility(View.VISIBLE);
            }else {
                nBtn.setText("");
                nBtn.setVisibility(View.GONE);
            }

            if(pBtnColor!=null && pBtnColor.length()>0) {
                GradientDrawable bgShape = (GradientDrawable)pBtn.getBackground();
                bgShape.setColor(Color.parseColor(pBtnColor));
            }

            if(nBtnColor!=null && nBtnColor.length()>0) {
                GradientDrawable bgShape = (GradientDrawable)nBtn.getBackground();
                bgShape.setColor(Color.parseColor(nBtnColor));
            }
            if(pListener!=null) {
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pListener.OnClick(dialog);
                    }
                });
            } else{
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }

                });
            }

            if(nListener!=null){
                nBtn.setVisibility(View.VISIBLE);
                nBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nListener.OnClick(dialog);
                    }
                });
            }else{
                nBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }

                });
            }
            dialog.show();

            return new CustomDialogue(this);

        }
    }

}

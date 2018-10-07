package com.testbook.arshad.testbookchatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.testbook.arshad.testbookchatapp.utils.LoadingIndicator;
import com.testbook.arshad.testbookchatapp.utils.dialogue.CustomDialogue;

/**
 * Created by arshad on 05/10/18.
 */

public class BaseActivity extends AppCompatActivity {

    private LoadingIndicator loadingIndicator;
    public static String username = "";
    public static String password = "";
    public static String chatWith = "";

    /**
     *
     * @param message loading message, default please wait
     * @param cancelable
     */
    public void showLoading(String message, boolean cancelable){
        if(message == null || message.length()<1){
            message = "Please wait...";
        }
        loadingIndicator = new LoadingIndicator.Builder(this)
                .setMessage(message)
                .isCancellable(cancelable)
                .build();
    }

    /**
     * hide loading indicator
     */
    public void hideLoadingIndicator(){
        if(loadingIndicator!=null)
            loadingIndicator.hideLoadingIndicator();
    }

    /**
     *
     * @param message toast message
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param title a title may be empty or null
     * @param message content may be empty or null
     * @param button button text is ok if null
     */
    public void showDefaultAlert(String title, String message, String button){
        new CustomDialogue.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveBtnText(button)
                .isCancellable(true)
                .build();
    }

    /**
     *
     * @param editText
     * @return text from edittext
     */
    public String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

}

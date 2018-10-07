package com.testbook.arshad.testbookchatapp.contract;

import com.android.volley.RequestQueue;
import com.firebase.client.Firebase;
import com.testbook.arshad.testbookchatapp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Arshad on 6/10/18.
 */

public interface ChatActivityContract {

    interface View extends BaseView {

        void showMessage(String message);

        void clearTextField();
    }

    interface Presenter extends BasePresenter<View> {
        void sendMessage(String message, String user, Firebase reference1, Firebase reference2);
    }
}

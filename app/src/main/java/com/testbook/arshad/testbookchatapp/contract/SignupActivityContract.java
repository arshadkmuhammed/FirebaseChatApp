package com.testbook.arshad.testbookchatapp.contract;

import com.android.volley.RequestQueue;
import com.testbook.arshad.testbookchatapp.presenter.BasePresenter;

/**
 * Created by Arshad on 6/10/18.
 */

public interface SignupActivityContract {

    interface View extends BaseView {

        void navigateToLoginActivity();

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter<View> {
        void doRegister(RequestQueue requestQueue, String username);
    }
}

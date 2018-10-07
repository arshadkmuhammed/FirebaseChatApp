package com.testbook.arshad.testbookchatapp.contract;

import com.android.volley.RequestQueue;
import com.testbook.arshad.testbookchatapp.presenter.BasePresenter;

public interface LoginActivityContract {

    interface View extends BaseView {

        void navigateToListActivity();

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter<View> {
        void doLogin(RequestQueue requestQueue, String username, String password);
    }
}

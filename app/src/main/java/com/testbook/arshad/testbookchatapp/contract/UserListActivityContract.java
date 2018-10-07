package com.testbook.arshad.testbookchatapp.contract;

import com.android.volley.RequestQueue;
import com.testbook.arshad.testbookchatapp.presenter.BasePresenter;

import java.util.ArrayList;

public interface UserListActivityContract {
    interface View extends BaseView {

        void showMessage(String message);

        void setUserList(ArrayList<String> userList);

    }

    interface Presenter extends BasePresenter<UserListActivityContract.View> {
        void fetchUsersList(RequestQueue requestQueue);
    }
}

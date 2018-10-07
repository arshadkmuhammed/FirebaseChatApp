package com.testbook.arshad.testbookchatapp.presenter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.testbook.arshad.testbookchatapp.activity.BaseActivity;
import com.testbook.arshad.testbookchatapp.contract.UserListActivityContract;
import com.testbook.arshad.testbookchatapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UserListActivityPresenter implements UserListActivityContract.Presenter {

    private UserListActivityContract.View mView;

    @Override
    public void attach(UserListActivityContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }


    /**
     * Call user list api
     * @param requestQueue Volley instance
     */
    @Override
    public void fetchUsersList(RequestQueue requestQueue) {
        if (mView != null) {
            mView.showProgressDialog(true);
        } else return;
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (mView != null) {
                    mView.showProgressDialog(false);
                } else return;
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mView != null) {
                    mView.showProgressDialog(false);
                } else return;
            }
        });
        requestQueue.add(request);
    }


    /**
     * Itrate users
     * @param s
     */
    public void doOnSuccess(String s) {
        ArrayList<String> users = new ArrayList<>();
        int totalUsers = 0;
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(BaseActivity.username)) {
                    users.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            //show no users text
        } else {
            mView.setUserList(users);
        }
    }
}

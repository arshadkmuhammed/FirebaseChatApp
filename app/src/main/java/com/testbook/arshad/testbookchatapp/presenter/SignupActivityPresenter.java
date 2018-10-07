package com.testbook.arshad.testbookchatapp.presenter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.testbook.arshad.testbookchatapp.contract.SignupActivityContract;
import com.testbook.arshad.testbookchatapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arshad on 6/10/18.
 */

public class SignupActivityPresenter implements SignupActivityContract.Presenter {

    private SignupActivityContract.View mView;

    @Override
    public void attach(SignupActivityContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    /**
     * Call register api
     * @param requestQueue volley instance
     * @param username
     */
    @Override
    public void doRegister(RequestQueue requestQueue, final String username) {
        if (mView!=null){
            mView.showProgressDialog(true);
        }else return;
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if (mView!=null){
                    mView.showProgressDialog(false);
                }else return;
                Firebase reference = new Firebase("https://androidchatapp-76776.firebaseio.com/users");

                if(s.equals("null")) {
                    reference.child(username).child("password").setValue(username);
                    showMessage("Registration successful. Please login to continue.");
                    mView.navigateToLoginActivity();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(username)) {
                            reference.child(username).child("password").setValue(username);
                            showMessage("Registration successful. Please login to continue.");
                            mView.navigateToLoginActivity();
                        } else {
                            showMessage("Username already exists");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mView!=null){
                    showMessage("Something went wrong. Please try again later.");
                    mView.showProgressDialog(false);
                };
            }
        });
        requestQueue.add(request);
    }

    private void showMessage(String message){
        if(mView!=null){
            mView.showMessage(message);
        }
    }
}

package com.testbook.arshad.testbookchatapp.presenter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.testbook.arshad.testbookchatapp.activity.BaseActivity;
import com.testbook.arshad.testbookchatapp.contract.LoginActivityContract;
import com.testbook.arshad.testbookchatapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivityPresenter implements LoginActivityContract.Presenter {

    private LoginActivityContract.View mView;

    @Override
    public void attach(LoginActivityContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    /**
     * Call login api
     * @param requestQueue volley instance
     * @param username
     * @param password
     */
    @Override
    public void doLogin(RequestQueue requestQueue, final String username, final String password) {
        if (mView!=null){
            mView.showProgressDialog(true);
        }else return;
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if (mView!=null){
                    mView.showProgressDialog(false);
                }else return;
                if(s.equals("null")){
                    showMessage("Invalid username or password");
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                        if(!obj.has(username)){
                            showMessage("Invalid username or password");
                        }
                        else if(obj.getJSONObject(username).getString("password").equals(password)){
                            BaseActivity.username = username;
                            BaseActivity.password = password;
                            mView.navigateToListActivity();
                        }
                        else {
                           showMessage("Invalid username or password");
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
                }else return;
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

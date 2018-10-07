package com.testbook.arshad.testbookchatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

import com.android.volley.toolbox.Volley;
import com.testbook.arshad.testbookchatapp.R;
import com.testbook.arshad.testbookchatapp.contract.LoginActivityContract;
import com.testbook.arshad.testbookchatapp.presenter.LoginActivityPresenter;
import com.testbook.arshad.testbookchatapp.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by arshad on 05/10/18.
 */

public class LoginActivity extends BaseActivity implements LoginActivityContract.View {

    private Unbinder unbinder;
    private LoginActivityPresenter mPresenter;

    @BindView(R.id.til_username)
    TextInputLayout tilUsername;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unbinder = ButterKnife.bind(this);
        initPresenter();
        setupInitialValues();
        setupTextChangeListener();
    }

    private void initPresenter() {
        mPresenter = new LoginActivityPresenter();
        mPresenter.attach(this);
    }

    private void setupInitialValues(){
        tilPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }


    /**
     * check the text field validations
     * @return valid or not
     */
    private boolean isValid() {
        boolean isValid = true;

        if (getTextFromEditText(tilUsername.getEditText()).length() <= 0) {
            tilUsername.setError(getResources().getString(R.string.required_field));
            tilUsername.getEditText().requestFocus();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Initialize text change listeners
     */
    private void setupTextChangeListener() {

        tilPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilPassword.isErrorEnabled()) {
                    tilPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tilUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilUsername.isErrorEnabled()) {
                    tilUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * Perform login if data valid
     */
    @OnClick(R.id.txt_login)
    public void doLogin(){
        if(isValid()){
            if(AppUtils.isNetworkAvailable(this)) {
                mPresenter.doLogin(Volley.newRequestQueue(this), getTextFromEditText(tilUsername.getEditText()),
                        getTextFromEditText(tilUsername.getEditText()));
            }else {
                showMessage(getString(R.string.no_network));
            }
        }
    }

    /**
     * navigate to sign up screen
     */
    @OnClick(R.id.ll_bottom)
    public void navigateToSignup(){
        startActivity(new Intent(this, SignupActivity.class));
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * navigate to user list screen
     */
    @Override
    public void navigateToListActivity() {
        startActivity(new Intent(this, UsersListActivity.class));
        finish();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void showErrorMessage(String message, int code) {
        showDefaultAlert("Error", message, "Ok");
    }

    @Override
    public void showProgressDialog(boolean showDialog) {
        if (showDialog) {
            showLoading("Please wait...", true);
        } else {
            hideLoadingIndicator();
        }
    }
}

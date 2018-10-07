package com.testbook.arshad.testbookchatapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.testbook.arshad.testbookchatapp.R;
import com.testbook.arshad.testbookchatapp.contract.SignupActivityContract;
import com.testbook.arshad.testbookchatapp.presenter.SignupActivityPresenter;
import com.testbook.arshad.testbookchatapp.utils.AppUtils;
import com.testbook.arshad.testbookchatapp.utils.CircleImageView;
import com.testbook.arshad.testbookchatapp.utils.Constants;
import com.testbook.arshad.testbookchatapp.utils.PreferenceManager;
import com.testbook.arshad.testbookchatapp.utils.dialogue.CustomDialogue;
import com.testbook.arshad.testbookchatapp.utils.dialogue.CustomDialogueClickListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Arshad on 6/10/18.
 */

public class SignupActivity extends BaseActivity implements SignupActivityContract.View {


    private Unbinder unbinder;

    private final int KEY_STORAGE_PERMISSION = 12;

    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;
    @BindView(R.id.til_last_name)
    TextInputLayout tilLastName;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;

    @BindView(R.id.img_user)
    CircleImageView imgUser;

    @BindView(R.id.img_camera)
    ImageView imgCamera;

    @BindView(R.id.ll_bottom)
    LinearLayout llLogin;

    @BindView(R.id.txt_terms_privacy)
    TextView txtTerms;

    @BindView(R.id.txt_signup)
    TextView txtSignup;

    private SignupActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(this);
        initPresenter();
        setupToolbar();
        initFirebase();
        setupTextChangeListner();
        checkForProfile();
    }

    /**
     * Initialize presenter
     */
    private void initPresenter() {
        mPresenter = new SignupActivityPresenter();
        mPresenter.attach(this);
    }

    /**
     * Set up toolbar
     */
    private void setupToolbar(){
        getSupportActionBar().setTitle("Sign up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    /**
     * Initialize firebase context
     */
    private void initFirebase(){
        Firebase.setAndroidContext(this);
    }

    /**
     * Check whether navigate from login screen or profile
     * If from profile, all edit fields are disable and show the cached values
     */
    private void checkForProfile(){
        if(getIntent() !=null){
            if(getIntent().getIntExtra(Constants.KEY_FROM,0) == 1){
                tilFirstName.getEditText().setText(PreferenceManager.getInstance(this).getFirstName());
                tilLastName.getEditText().setText(PreferenceManager.getInstance(this).getLastName());
                tilUsername.getEditText().setText(PreferenceManager.getInstance(this).getUsername());

                byte[] decodedString = Base64.decode(PreferenceManager.getInstance(this).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if(decodedByte!= null){
                    imgUser.setImageBitmap(decodedByte);
                }
                tilUsername.setEnabled(false);
                tilFirstName.setEnabled(false);
                tilLastName.setEnabled(false);
                imgUser.setEnabled(false);
                imgCamera.setEnabled(false);
                imgCamera.setVisibility(View.GONE);
                llLogin.setVisibility(View.GONE);
                txtTerms.setVisibility(View.GONE);
                txtSignup.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Profile");
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.ll_bottom)
    public void onBackClick(){
        onBackPressed();
    }

    /**
     * Capture image from Gallery or Camera
     * Require external storage permission
     */
    @OnClick({R.id.img_user, R.id.img_camera})
    public void captureImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showPermissionAlert();
            } else {
                startImageCrop();
            }
        }else {
            startImageCrop();
        }
    }

    /**
     * Perform registration if data are valid
     */
    @OnClick(R.id.txt_signup)
    public void doRegister(){
        if (isValid()){
            if(AppUtils.isNetworkAvailable(this)) {
                mPresenter.doRegister(Volley.newRequestQueue(this),getTextFromEditText(tilUsername.getEditText()));
                cacheUserData();
            }else {
                showMessage(getString(R.string.no_network));
            }
        }
    }

    /**
     * Save user data in shared preference
     */
    private void cacheUserData() {
        PreferenceManager.getInstance(this).saveFirstName(getTextFromEditText(tilFirstName.getEditText()));
        PreferenceManager.getInstance(this).saveLastName(getTextFromEditText(tilLastName.getEditText()));
        PreferenceManager.getInstance(this).saveUsername(getTextFromEditText(tilUsername.getEditText()));
    }

    /**
     * Initialize text change listener
     */
    private void setupTextChangeListner(){
        tilFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(tilFirstName.isErrorEnabled()){
                    tilFirstName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tilLastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(tilLastName.isErrorEnabled()){
                    tilLastName.setErrorEnabled(false);
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
                if(tilUsername.isErrorEnabled()){
                    tilUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * invoke crop image activity
     */
    private void startImageCrop() {
        CropImage.activity()
                .setActivityTitle("Crop")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setFixAspectRatio(true)
                .setAllowFlipping(false)
                .start(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setCropImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == KEY_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageCrop();
            }
        }
    }

    /**
     * Set image from uri
     * @param imageUri
     */
    private void setCropImage(Uri imageUri) {
        imgUser.setImageURI(imageUri);
        new SaveImageAsString().execute(imageUri);
    }


    /**
     * Alert for grant storage permission
     */
    private void showPermissionAlert(){
        new CustomDialogue.Builder(this)
                .setTitle("Grant Permission")
                .setMessage("To set your profile picture, you need to grant the external storage permission.")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnText("Okey")
                .isCancellable(false)
                .OnPositiveClicked(new CustomDialogueClickListener() {
                    @Override
                    public void OnClick(Dialog dialog) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, KEY_STORAGE_PERMISSION);
                    }
                })
                .OnNegativeClicked(new CustomDialogueClickListener() {
                    @Override
                    public void OnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
    }

    /**
     * Save Image as string
     */
    private class SaveImageAsString extends AsyncTask<Uri, Void, Void>{

        @Override
        protected Void doInBackground(Uri... uris) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uris[0]);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = AppUtils.encodeImage(selectedImage);
                PreferenceManager.getInstance(SignupActivity.this).saveImage(encodedImage);
            } catch (FileNotFoundException e) {
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Check for validity
     * @return valid or not
     */
    private boolean isValid() {
        boolean isValid = true;

        if (getTextFromEditText(tilUsername.getEditText()).length() <= 0) {
            tilUsername.setError(getResources().getString(R.string.required_field));
            tilUsername.getEditText().requestFocus();
            isValid = false;
        }

        if (getTextFromEditText(tilLastName.getEditText()).length() <= 0) {
            tilLastName.setError(getResources().getString(R.string.required_field));
            tilLastName.getEditText().requestFocus();
            isValid = false;
        }
        if (getTextFromEditText(tilFirstName.getEditText()).length() <= 0) {
            tilFirstName.setError(getResources().getString(R.string.required_field));
            tilFirstName.getEditText().requestFocus();
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void navigateToLoginActivity() {
        onBackPressed();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void showErrorMessage(String message, int code) {

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

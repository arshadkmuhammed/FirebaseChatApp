package com.testbook.arshad.testbookchatapp.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.testbook.arshad.testbookchatapp.R;
import com.testbook.arshad.testbookchatapp.contract.ChatActivityContract;
import com.testbook.arshad.testbookchatapp.presenter.ChatActivityPresenter;
import com.testbook.arshad.testbookchatapp.utils.Constants;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Arshad on 6/10/18.
 */

public class ChatActivity extends BaseActivity implements ChatActivityContract.View {

    private Unbinder unbinder;
    private ChatActivityPresenter mPresenter;
    private Firebase reference1, reference2;

    @BindView(R.id.ll_chat_container)
    LinearLayout layout;
    @BindView(R.id.edt_message_area)
    EditText edtMessageArea;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        unbinder = ButterKnife.bind(this);
        initPresenter();
        initFirebase();
        setupToolbar();
        initListener();
    }

    /**
     * Initialize firebase context
     */
    private void initFirebase() {
        Firebase.setAndroidContext(this);
        reference1 = new Firebase(Constants.MESSAGE_URL + BaseActivity.username + "_" + BaseActivity.chatWith);
        reference2 = new Firebase(Constants.MESSAGE_URL+ BaseActivity.chatWith + "_" + BaseActivity.username);

    }

    /**
     * setup toolbar
     */
    private void setupToolbar(){
        getSupportActionBar().setTitle(BaseActivity.chatWith);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * send message to another user
     */
    @OnClick(R.id.img_send)
    public void sendMessage(){
        if(edtMessageArea.getText().toString().length()>0){
            mPresenter.sendMessage(edtMessageArea.getText().toString(), BaseActivity.username, reference1, reference2);
        }
    }

    /**
     * initialize presenter
     */
    private void initPresenter() {
        mPresenter = new ChatActivityPresenter();
        mPresenter.attach(this);
    }

    /**
     * Initialize Firebase Listener
     */
    private void initListener() {
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(BaseActivity.username)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(BaseActivity.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    /**
     *
     * @param message chat message
     * @param type decides the gravity of the chat
     */
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_yellow);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_green);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * Clear previously sent message
     */
    @Override
    public void clearTextField() {
        edtMessageArea.setText("");
    }

    @Override
    public void showErrorMessage(String message, int code) {

    }

    @Override
    public void showProgressDialog(boolean showDialog) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}

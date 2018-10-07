package com.testbook.arshad.testbookchatapp.presenter;

import com.android.volley.RequestQueue;
import com.firebase.client.Firebase;
import com.testbook.arshad.testbookchatapp.activity.BaseActivity;
import com.testbook.arshad.testbookchatapp.contract.ChatActivityContract;
import com.testbook.arshad.testbookchatapp.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arshad on 6/10/18.
 */

public class ChatActivityPresenter  implements ChatActivityContract.Presenter {

    private ChatActivityContract.View mView;


    @Override
    public void attach(ChatActivityContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    /**
     * Send message to user
     * @param message message to be send
     * @param user recipient
     * @param reference1 firebase reference
     * @param reference2 firebase reference
     */
    @Override
    public void sendMessage(String message, String user,
                            Firebase reference1, Firebase reference2) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", message);
        map.put("user", user);
        reference1.push().setValue(map);
        reference2.push().setValue(map);

        if(mView!=null)
            mView.clearTextField();
    }
}

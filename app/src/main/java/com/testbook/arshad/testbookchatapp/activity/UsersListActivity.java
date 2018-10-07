package com.testbook.arshad.testbookchatapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.Volley;
import com.testbook.arshad.testbookchatapp.R;
import com.testbook.arshad.testbookchatapp.adapter.UserListAdapter;
import com.testbook.arshad.testbookchatapp.contract.UserListActivityContract;
import com.testbook.arshad.testbookchatapp.presenter.UserListActivityPresenter;
import com.testbook.arshad.testbookchatapp.utils.AppUtils;
import com.testbook.arshad.testbookchatapp.utils.Constants;
import com.testbook.arshad.testbookchatapp.utils.dialogue.CustomDialogue;
import com.testbook.arshad.testbookchatapp.utils.dialogue.CustomDialogueClickListener;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsersListActivity extends BaseActivity implements UserListActivityContract.View {

    private UserListActivityPresenter mPresenter;
    private Unbinder unbinder;
    private LinearLayoutManager layoutManager;
    private boolean isLastPage;
    private ArrayList<String> userList = new ArrayList<>();
    private int pageCount = 0;
    private UserListAdapter adapter;

    @BindView(R.id.recyclerview_users)
    RecyclerView recyclerView;

    @BindView(R.id.progressView)
    GeometricProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        unbinder = ButterKnife.bind(this);
        initPresenter();
        fetchList();
    }

    /**
     * Initialize presenter
     */
    private void initPresenter() {
        mPresenter = new UserListActivityPresenter();
        mPresenter.attach(this);
    }

    /**
     * fetch user list
     */
    private void fetchList() {
        if(AppUtils.isNetworkAvailable(this)) {
            mPresenter.fetchUsersList(Volley.newRequestQueue(this));
        }else {
            showMessage(getString(R.string.no_network));
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * set user list
     * @param list list of available users
     */
    @Override
    public void setUserList(final ArrayList<String> list) {
        userList = list;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        adapter = new UserListAdapter(this, getListItems(pageCount));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new UserListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                BaseActivity.chatWith = userList.get(position);
                startActivity(new Intent(UsersListActivity.this, ChatActivity.class));
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                showLogoutAlert();
                break;
            case R.id.menu_profile:
                navigateToProfile();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Ask for confirmation to logout
     */
    private void showLogoutAlert() {
        new CustomDialogue.Builder(this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_message))
                .setPositiveBtnText(getString(R.string.okey))
                .setNegativeBtnText(getString(R.string.cancel))
                .isCancellable(true)
                .OnPositiveClicked(new CustomDialogueClickListener() {
                    @Override
                    public void OnClick(Dialog dialog) {
                        startActivity(new Intent(UsersListActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .build();
    }

    /**
     * Navigate to Sign up screen with value 1 to indicate profile
     */
    private void navigateToProfile() {
        Intent intent = new Intent(UsersListActivity.this, SignupActivity.class);
        intent.putExtra(Constants.KEY_FROM, 1);
        startActivity(intent);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= 25) {
                            progressView.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreItems();
                                }
                            }, 3000);

                        }
                    }
                }
            };

    private void loadMoreItems(){
        progressView.setVisibility(View.GONE);
        adapter.addItems(getListItems(pageCount));
    }

    /**
     *
     * @param page page number
     * @return  25 items from main list
     */
    private ArrayList<String> getListItems(int page) {
        ArrayList<String> list = new ArrayList<>();
        if (pageCount < 25) {
            for (int i = page * 25; i < (page * 25) + 25; i++) {
                list.add(userList.get(i));
            }
            pageCount++;
        }
        return list;
    }
}

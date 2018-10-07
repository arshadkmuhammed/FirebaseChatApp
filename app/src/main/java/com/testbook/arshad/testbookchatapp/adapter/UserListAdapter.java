package com.testbook.arshad.testbookchatapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testbook.arshad.testbookchatapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private ArrayList<String> list;
    private Context context;
    public MyClickListener myClickListener;


    public UserListAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }

    public void addItems(ArrayList<String> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View profileItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_list_item, parent, false);

        return new MyViewHolder(profileItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtName.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getPosition(), view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View view);
    }
}

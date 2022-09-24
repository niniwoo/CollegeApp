package com.example.collegegram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<Users> usersList = new ArrayList();
    UserClick postClick;

    public UsersRecyclerAdapter(Context context, ArrayList<Users> usersList, UserClick postClick){
        this.context = context;
        this.usersList = usersList;
        this.postClick = postClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_users_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(usersList.get(position).firstname+" "+usersList.get(position).lastname);
        holder.tvDob.setText(usersList.get(position).dob);
        holder.tvEmail.setText(usersList.get(position).email);
        holder.tvRoleType.setText(usersList.get(position).role);
        holder.tvProgram.setText(usersList.get(position).program);
        if(usersList.get(position).isApproved == 1){
            holder.tvIsApproved.setText("Approved");
        }else{
            holder.tvIsApproved.setText("Disabled");

        }
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postClick.userClick(usersList.get(position));
                    }
                }
        );
        holder.tvIsApproved.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postClick.approveUser(usersList.get(position));
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvEmail, tvProgram, tvDob, tvRoleType, tvIsApproved;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvIsApproved = (TextView) view.findViewById(R.id.tvApproved);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvProgram = (TextView) view.findViewById(R.id.tvProgram);
            tvDob = (TextView) view.findViewById(R.id.tvDob);
            tvRoleType = (TextView) view.findViewById(R.id.tvRoleType);
        }
    }

}

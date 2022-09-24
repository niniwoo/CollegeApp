package com.example.collegegram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPostsRecyclerAdapter extends RecyclerView.Adapter<MyPostsRecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<Posts> postsList = new ArrayList();
    PostClick postClick;

    public MyPostsRecyclerAdapter(Context context, ArrayList<Posts> postsList, PostClick postClick){
        this.context = context;
        this.postsList = postsList;
        this.postClick = postClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_my_posts, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] imgByte = postsList.get(position).imgByte;
        Log.e("TAG", " imageByte "+imgByte);
        if(imgByte != null) {
            Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            holder.imageView.setImageBitmap(image);
            holder.imageView.setVisibility(View.VISIBLE);

        }else{
            holder.imageView.setVisibility(View.GONE);
        }
        holder.tvPost.setText(postsList.get(position).post);
        holder.tvCreatedDate.setText(postsList.get(position).date);
        if(postsList.get(position).isApproved == 1){
            holder.tvIsApproved.setText("Approved");
        }else{
            holder.tvIsApproved.setText("Pending");

        }
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postClick.postClick(postsList.get(position));
                    }
                }
        );
        holder.tvIsApproved.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postClick.approvePost(postsList.get(position));
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPost, tvCreatedDate, tvIsApproved;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            tvPost = (TextView) view.findViewById(R.id.tvPost);
            tvCreatedDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            tvIsApproved = (TextView) view.findViewById(R.id.tvApproved);
            imageView = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

}

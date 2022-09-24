package com.example.collegegram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    ArrayList<Posts> postsList = new ArrayList();
    PostClick postClick;

    public RecyclerAdapter(Context context, ArrayList<Posts> postsList, PostClick postClick){
        this.context = context;
        this.postsList = postsList;
        this.postClick = postClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_posts, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] imgByte = postsList.get(position).imgByte;
        Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        holder.tvPost.setText(postsList.get(position).post);
        holder.tvCreatedDate.setText(postsList.get(position).date);
        holder.ivImage.setImageBitmap(image);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postClick.postClick(postsList.get(position));
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPost, tvCreatedDate;
        public ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            tvPost = (TextView) view.findViewById(R.id.tvPost);
            tvCreatedDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

}

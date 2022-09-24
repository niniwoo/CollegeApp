package com.example.collegegram;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAllPosts extends AppCompatActivity implements PostClick{
    private DatabaseHelper databaseHelper;
    private MyPostsRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Posts> postsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerAdapter = new MyPostsRecyclerAdapter(this, postsList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
       getPosts();
        Log.e("TAG", "post list "+postsList.size());
    }

    private void getPosts() {
        Cursor cursor = databaseHelper.getSignedInUser();
        if(cursor != null && cursor.getCount()>0){
            {
                if(cursor.moveToFirst()) {
                    do {
                        userId = cursor.getInt(cursor.getColumnIndex("Id"));
                    } while (cursor.moveToNext());
                }
                cursor.close();

            }
        }
        postsList.addAll( databaseHelper.getAllPosts());
        recyclerAdapter.notifyDataSetChanged();

    }

    public void enterCreatePost(View view){
        Intent intent = new Intent(AdminAllPosts.this, AddComment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void postClick(Posts posts) {
        Intent intent = new Intent(AdminAllPosts.this, PostDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", posts.getId());
        startActivity(intent);

    }

    @Override
    public void approvePost(Posts posts) {
        if(posts.isApproved == 1){
            Toast.makeText(AdminAllPosts.this, "Post is already approved", Toast.LENGTH_SHORT).show();

            return;
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAllPosts.this);
        alertDialog.setTitle("Approve Post");
        alertDialog.setMessage("Are you sure you want to approve this post");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int cursor = databaseHelper.approvePost(posts.id, 1);
                if(cursor > 0) {
                    Toast.makeText(AdminAllPosts.this, "Post approved successfully", Toast.LENGTH_SHORT).show();
                    postsList.clear();
                    getPosts();
                }
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();

    }
}
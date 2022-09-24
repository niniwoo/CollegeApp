package com.example.collegegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Forum extends AppCompatActivity implements PostClick{
    private DatabaseHelper databaseHelper;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Posts> postsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ImageView noPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        noPosts = findViewById(R.id.noPosts);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerAdapter = new RecyclerAdapter(this, postsList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        postsList.addAll( databaseHelper.getPosts());
        Log.e("TAG", "post list "+postsList.size());
        recyclerAdapter.notifyDataSetChanged();
        if(postsList.isEmpty()){
            noPosts.setVisibility(View.VISIBLE);
        }else{
            noPosts.setVisibility(View.GONE);
        }
    }

    public void enterCreatePost(View view){
        startActivity(new Intent(Forum.this, AddComment.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuMyPosts) {
            Intent intent = new Intent(Forum.this, MyPosts.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menuLogout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Forum.this);
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are you sure you want to logout from the app");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Cursor cursor = databaseHelper.getSignedInUser();
                    if(cursor != null && cursor.getCount()>0){
                        {
                            if (cursor.moveToFirst()){
                                do{
                                    int isUpdated = databaseHelper.updateUserInformation(cursor.getString(cursor.getColumnIndex("Email")),cursor.getString(cursor.getColumnIndex("Password")), 0);
                                    if(isUpdated > 0) {
                                        Toast.makeText(Forum.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Forum.this, LoginPage.class);
                                        startActivity(intent);

                                    }
                                }while(cursor.moveToNext());
                            }
                            cursor.close();
                        }
                    }


                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void postClick(Posts posts) {
        Intent intent = new Intent(Forum.this, PostDetailsActivity.class);
        intent.putExtra("id", posts.getId());
        startActivity(intent);

    }

    @Override
    public void approvePost(Posts posts) {

    }
}
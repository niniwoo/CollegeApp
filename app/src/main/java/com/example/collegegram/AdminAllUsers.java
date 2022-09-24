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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAllUsers extends AppCompatActivity implements UserClick{
    private DatabaseHelper databaseHelper;
    private UsersRecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private ImageView ivEmpty;
    private ArrayList<Users> usersList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_users);
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        ivEmpty = findViewById(R.id.noUsers);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerAdapter = new UsersRecyclerAdapter(this, usersList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
       getAllUsers();
    }

    private void getAllUsers() {
        usersList.addAll( databaseHelper.getAllUsers());
        recyclerAdapter.notifyDataSetChanged();
        if(usersList.isEmpty()){
            ivEmpty.setVisibility(View.VISIBLE);
        }else{
            ivEmpty.setVisibility(View.GONE);

        }

    }

    public void createUser(View view){
        Intent intent = new Intent(AdminAllUsers.this, CreateAccountAdmin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void userClick(Users posts) {
        Intent intent = new Intent(AdminAllUsers.this, CreateAccountAdmin.class);
        intent.putExtra("id", posts.getId());
        startActivity(intent);

    }

    @Override
    public void approveUser(Users posts) {
        String title = "Approve User";
        String msg = " Do you want to approve the user";
        int isApproved = 1;
        if(posts.isApproved == 1){
            isApproved = 0;
            title = "Disable User";
            msg = " Do you want to disapprove the user";

        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminAllUsers.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        int finalIsApproved = isApproved;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int cursor = databaseHelper.approveDisableUser(posts.id, finalIsApproved);
                if(cursor > 0) {
                    Toast.makeText(AdminAllUsers.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    usersList.clear();
                    getAllUsers();
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
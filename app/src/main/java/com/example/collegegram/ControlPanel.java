package com.example.collegegram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ControlPanel extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        databaseHelper = new DatabaseHelper(this);

    }

    public void enterAdminPostPanel(View view){
        startActivity(new Intent(ControlPanel.this, AddComment.class));

    }
    public void enterAdminControlUserPanel(View view){
        startActivity(new Intent(ControlPanel.this, AdminAllUsers.class));

    }

    public void checkPosts(View view) {
        startActivity(new Intent(ControlPanel.this, AdminAllPosts.class));

    }

    public void logout(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
                                        Toast.makeText(ControlPanel.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ControlPanel.this, LoginPage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);

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


    }
}
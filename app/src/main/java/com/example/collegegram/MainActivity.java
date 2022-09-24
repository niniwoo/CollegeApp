package com.example.collegegram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        Button button=findViewById(R.id.btnStart);
        databaseHelper.isAdminExists();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = databaseHelper.getSignedInUser();
                String role = "";
                if(cursor != null && cursor.getCount()>0){
                        {
                            if(cursor.moveToFirst()) {
                                do {
                                    role = cursor.getString(cursor.getColumnIndex("Role"));
                                } while (cursor.moveToNext());
                            }
                        }
                    cursor.close();
                    if(role.toLowerCase().equals("admin")){
                        Intent intent = new Intent(getApplicationContext(), ControlPanel.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), Forum.class);
                        startActivity(intent);
                        finish();
                    }
                }else
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
            }
        });
    }
}
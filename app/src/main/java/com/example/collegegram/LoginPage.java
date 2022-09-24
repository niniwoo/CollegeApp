package com.example.collegegram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        databaseHelper = new DatabaseHelper(this);
        EditText emailAddress = findViewById(R.id.txtEmail);
        EditText password = findViewById(R.id.txtPassword);

        Button button=findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            String emailAdd,pass;
            @Override
            public void onClick(View view) {
                emailAdd = emailAddress.getText().toString();
                pass = password.getText().toString();
                if(emailAdd.isEmpty()){
                    Toast.makeText(LoginPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.isEmpty()){
                    Toast.makeText(LoginPage.this, "Enter pass", Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean checkuser = databaseHelper.checkEmailPassword(emailAdd, pass);
                if(checkuser){
                    int isExists = databaseHelper.updateUserInformation(emailAdd,pass, 1);
                    int isApproved = 0;
                    String role = "";
                    if(isExists > 0) {
                        Cursor cursor = databaseHelper.getSignedInUser();
                        if(cursor != null && cursor.getCount()>0){
                            {
                                if(cursor.moveToFirst()) {
                                    do {
                                       isApproved = cursor.getInt(cursor.getColumnIndex("isApproved"));
                                       role = cursor.getString(cursor.getColumnIndex("Role"));
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();

                            }
                        }
                        if(isApproved == 1) {
                            Toast.makeText(LoginPage.this, "Log In successfully", Toast.LENGTH_SHORT).show();

                            if(role.toLowerCase().equals("admin")){
                                Intent intent = new Intent(getApplicationContext(), ControlPanel.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), Forum.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }else{
                            Toast.makeText(LoginPage.this, "Sorry your account is disabled by admin", Toast.LENGTH_SHORT).show();

                        }

                    }

                }else{
                    Toast.makeText(LoginPage.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void enterRegistration(View view){
        Intent intent = new Intent(LoginPage.this, CreateAccount.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
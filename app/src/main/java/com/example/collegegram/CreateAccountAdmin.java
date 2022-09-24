package com.example.collegegram;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAccountAdmin extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText emailAddress,password,nameF, nameL, program;
    TextView date, txtCreateAccount;
    Button register;
    RadioButton student, staff;
    final Calendar myCalendar= Calendar.getInstance();
    int id = -1;
    Users users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        databaseHelper = new DatabaseHelper(this);
        student = findViewById(R.id.radioStudent);
        staff = findViewById(R.id.radioStaff);
        txtCreateAccount = findViewById(R.id.txtCreateAccount);
        emailAddress = findViewById(R.id.txtEmailRegsiter);
        password = findViewById(R.id.txtPassRegister);
        nameF = findViewById(R.id.txtBoxFName);
        nameL = findViewById(R.id.txtBoxLName);
        date = findViewById(R.id.txtBoxDate);
        program = findViewById(R.id.txtBoxProgram);
        register = findViewById(R.id.btnRegister);
        id = getIntent().getIntExtra("id", -1);
        if(id>=0){
            users = databaseHelper.getSingleUser(id);
            emailAddress.setText(users.email);
            nameF.setText(users.firstname);
            nameL.setText(users.lastname);
            date.setText(users.dob);
            program.setText(users.program);
            password.setVisibility(View.GONE);
            if(users.role.toLowerCase().equals("student")){
                student.setChecked(true);
                staff.setChecked(false);
            }else if(users.role.toLowerCase().equals("staff")){
                student.setChecked(false);
                staff.setChecked(true);
            }
            register.setVisibility(View.GONE);
            txtCreateAccount.setText("Account Details");

        }else{
            password.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            register.setText("Create Account");
            txtCreateAccount.setText("Create Account");


        }


        DatePickerDialog.OnDateSetListener dateDialog =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="MM/dd/yy";
                SimpleDateFormat dateFormat =new SimpleDateFormat(myFormat, Locale.getDefault());
                date.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

         date.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 new DatePickerDialog(CreateAccountAdmin.this,dateDialog,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

             }
         });

        register.setOnClickListener(new View.OnClickListener() {
            String email,pswd,fname,lname,dob,selectedProgram,radioInfo;
            @Override
            public void onClick(View view) {
                email = emailAddress.getText().toString();
                pswd = password.getText().toString();
                fname = nameF.getText().toString();
                lname = nameL.getText().toString();
                dob = date.getText().toString();
                selectedProgram = program.getText().toString();

                if(student.isChecked())
                {
                    radioInfo = "student";
                }
                else if(staff.isChecked()){
                    radioInfo = "staff";
                }else{
                    Toast.makeText(CreateAccountAdmin.this, "Select role", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pswd.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fname.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(lname.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dob.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedProgram.isEmpty()){
                    Toast.makeText(CreateAccountAdmin.this, "Enter program", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean checkuser = databaseHelper.checkEmail(email);
                if(!checkuser){

                    Boolean insert = databaseHelper.insertData(email,fname, lname,pswd,dob, selectedProgram,radioInfo, 1);
                    if(insert){
                        Toast.makeText(CreateAccountAdmin.this, "User created successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    }else{
                        Toast.makeText(CreateAccountAdmin.this, "Sorry! user is not created", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreateAccountAdmin.this, "Sorry! user is not created as email is associated with other account", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
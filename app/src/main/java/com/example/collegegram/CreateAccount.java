package com.example.collegegram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAccount extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText emailAddress,password,nameF, nameL, program;
    TextView date;
    Button register;
    RadioButton student, staff;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.alertTable();
         student = findViewById(R.id.radioStudent);
         staff = findViewById(R.id.radioStaff);
         emailAddress = findViewById(R.id.txtEmailRegsiter);
         password = findViewById(R.id.txtPassRegister);
         nameF = findViewById(R.id.txtBoxFName);
         nameL = findViewById(R.id.txtBoxLName);
         date = findViewById(R.id.txtBoxDate);
         program = findViewById(R.id.txtBoxProgram);
         register = findViewById(R.id.btnRegister);

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
                 new DatePickerDialog(CreateAccount.this,dateDialog,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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
                }

                if(email.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pswd.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fname.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(lname.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dob.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Select Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedProgram.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Enter program", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean checkuser = databaseHelper.checkEmail(email);
                if(!checkuser){

                    Boolean insert = databaseHelper.insertData(email,fname, lname,pswd,dob, selectedProgram,radioInfo, 1);
                    if(insert){
                        int cursor = databaseHelper.updateUserInformation(email,pswd, 1);
                        if(cursor > 0) {
                                Toast.makeText(CreateAccount.this, "Registered In successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Forum.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                        }
                    }else{
                        Toast.makeText(CreateAccount.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
package com.example.collegegram;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddComment extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText adminMsg;
    private Button button;
    private ImageButton adminImg;
    private ImageView ivImage;
    Bitmap bitmap= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        databaseHelper = new DatabaseHelper(this);
         adminMsg = findViewById(R.id.msg);
         ivImage = findViewById(R.id.ivImage);

         button=findViewById(R.id.btnAdminPost);
        adminImg=findViewById(R.id.adminImg);

        adminImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermission();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            String message;
            @Override
            public void onClick(View view) {
                message = adminMsg.getText().toString();
                byte[] data = new byte[0];
                if(message.isEmpty()){
                    Toast.makeText(AddComment.this, "Enter Message to post", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(calendar.getTime());
                if(bitmap != null){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    data = outputStream.toByteArray();
                }
                Cursor cursor = databaseHelper.getSignedInUser();
                if(cursor != null && cursor.getCount()>0){
                    {
                        if(cursor.moveToFirst()) {
                            do {
                                String role = cursor.getString(cursor.getColumnIndex("Role"));
                                int isApproved = 0;
                                if(role.toLowerCase().equals("admin")){
                                    isApproved = 1;
                                }
                                Boolean insert = databaseHelper.insertPost(cursor.getInt(cursor.getColumnIndex("Id")), message,data, date, isApproved );
                                if (insert) {
                                    Toast.makeText(AddComment.this, "Post posted successfully", Toast.LENGTH_SHORT).show();
                                    if(isApproved != 1) {
                                        Intent intent = new Intent(AddComment.this, Forum.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(AddComment.this, "Cannot add post", Toast.LENGTH_SHORT).show();
                                }
                            } while (cursor.moveToNext());
                        }
                        cursor.close();

                    }
            }
        }});
    }

    public void cancelBtn(View view){
        finish();
    }

    private void pickImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        ivImage.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            pickImage();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    boolean externalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (externalStorage){
                        pickImage();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow permission to select image from storage",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                                                            1);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddComment.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
package com.example.collegegram;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private int id;
    private Posts posts;
    private Users user;
    private Button btnUpdate, btnLike;
    private ImageView ivImage;
    TextView msg, txtTitle, tvPostedBy;
    Bitmap bitmap = null;
    int postLikes = 0;
    int userId = -1;
    boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        databaseHelper = new DatabaseHelper(this);
        msg = findViewById(R.id.msg);
        txtTitle = findViewById(R.id.txtTitle);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvPostedBy = findViewById(R.id.tvPostedBy);
        btnLike = findViewById(R.id.btnLike);
        ivImage = findViewById(R.id.ivImage);

        id = getIntent().getIntExtra("id", -1);
        refreshData();

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLiked) {
                    if (databaseHelper.unlikePost(id, userId)) {
                        refreshData();
                    }
                } else {
                    if (databaseHelper.likePost(id, userId)) {
                        refreshData();
                    }
                }
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             byte[] data = new byte[0];

                                             if (bitmap != null) {
                                                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                                 bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                                                 data = outputStream.toByteArray();
                                             } else {
                                                 data = posts.imgByte;
                                             }
                                             int cursor = databaseHelper.updatePost(posts.id, msg.getText().toString(), data);
                                             if (cursor > 0) {
                                                 Toast.makeText(PostDetailsActivity.this, "Post updated successfully. Please wait for admin approval", Toast.LENGTH_SHORT).show();
                                                 finish();

                                             }
                                         }
                                     }
        );
    }

    private void refreshData() {
        posts = databaseHelper.getSinglePost(id);
        postLikes = databaseHelper.getPostLikes(id);
        user = databaseHelper.getSingleUser(posts.userId);
        tvPostedBy.setText(user.firstname+" "+user.lastname);
        msg.setText(posts.post);
        txtTitle.setText("Likes " + String.valueOf(postLikes));

        if (posts.imgByte != null) {
            byte[] imgByte = posts.imgByte;
            Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            ivImage.setImageBitmap(image);
        }
        Cursor cursor = databaseHelper.getSignedInUser();
        if (cursor != null && cursor.getCount() > 0) {
            {
                if (cursor.moveToFirst()) {
                    do {
                        userId = cursor.getInt(cursor.getColumnIndex("Id"));
                        isLiked = databaseHelper.getLikeUnlikePost(id, userId);
                        if (posts.userId == cursor.getInt(cursor.getColumnIndex("Id"))) {
                            btnLike.setVisibility(View.GONE);
                        } else {
                            btnLike.setVisibility(View.VISIBLE);
                        }

                        if (posts.userId == cursor.getInt(cursor.getColumnIndex("Id")) && posts.isApproved == 0) {
                            btnUpdate.setVisibility(View.VISIBLE);
                            msg.setEnabled(true);
                        } else {
                            btnUpdate.setVisibility(View.GONE);
                            msg.setEnabled(false);
                        }

                    } while (cursor.moveToNext());
                }
                cursor.close();

            }
        }
        if (isLiked == true) {
            btnLike.setText("Unlike Post");
        } else {
            btnLike.setText("Like Post");

        }
    }

    private void pickImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
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

                    if (externalStorage) {
                        pickImage();
                    } else {
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
        new AlertDialog.Builder(PostDetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
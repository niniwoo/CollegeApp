package com.example.collegegram;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "Information.db";
    final static int DATABASE_VERSION = 3;
    final static String TABLE1_Name = "User";
    final static String T1COL1 = "Id";
    final static String T1COL2 = "fName";
    final static String T1COL3 = "lName";
    final static String T1COL4 = "DoB";
    final static String T1COL5 = "Password";
    final static String T1COL6 = "Program";
    final static String T1COL7 = "Role";
    final static String T1COL8 = "Email";
    final static String T1COL9 = "login";
    final static String T1COL10 = "isApproved";


    final static String TABLE2_Name = "POST";
    final static String T2COL1 = "Id";
    final static String T2COL2 = "userId";
    final static String T2COL3 = "post";
    final static String T2COL4 = "image";
    final static String T2COL5 = "likes";
    final static String T2COL6 = "createdDate";
    final static String T2COL7 = "isApproved";


    final static String TABLE3_Name = "LIKES";
    final static String T3COL1 = "Id";
    final static String T3COL2 = "userId";
    final static String T3COL3 = "postId";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE1_Name + "(" + T1COL1 + " INTEGER PRIMARY KEY," +
                T1COL2 + " TEXT," + T1COL3 + " TEXT," + T1COL4 + " Text," + T1COL5 +
                " Text," + T1COL6 + " TEXT," + T1COL7 + " Text," + T1COL8 + " Text," + T1COL9 + " INTEGER," + T1COL10 + " INTEGER)";
        db.execSQL(query);
        String postTable = "CREATE TABLE " + TABLE2_Name + "(" + T2COL1 + " INTEGER PRIMARY KEY," +
                T2COL2 + " INTEGER," + T2COL3 + " TEXT," + T2COL4 + " BLOB," + T2COL5 + " Text,"
                + T2COL6 + " Text," + T2COL7 + " INTEGER)";
        db.execSQL(postTable);
        String likesTable = "CREATE TABLE " + TABLE3_Name + "(" + T3COL1 + " INTEGER PRIMARY KEY," +
                T3COL2 + " INTEGER," + T3COL3 + " INTEGER)";
        db.execSQL(likesTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_Name);
        onCreate(db);
    }


    // to verify if the user exist or not
    public Boolean checkEmail(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from User where email = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // verify login
    public Boolean checkEmailPassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from User where email = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //getUserInformation
    public Integer updateUserInformation(String username, String password, int isLoggedIn) {
        int cursor = 0;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(T1COL9, isLoggedIn);
//        cursor = MyDB.update(TABLE1_Name, newValues, "email = '" + username + "' and password = " + password, null);
        cursor = MyDB.update(TABLE1_Name, newValues, "email = ? and password = ?", new String[]{username, password});
        return cursor;
    }

    public boolean insertData(String email, String firstName, String lastName, String password, String DoB, String program, String role, int isApproved) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(T1COL2, firstName);
            values.put(T1COL3, lastName);
            values.put(T1COL4, DoB);
            values.put(T1COL5, password);
            values.put(T1COL6, program);
            values.put(T1COL7, role);
            values.put(T1COL8, email);
            values.put(T1COL10, isApproved);
            Log.e("TAG", " role $role " + role);
            long result = db.insert(TABLE1_Name, null, values);
            if (result == -1)
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Cursor getSignedInUser() {
        Cursor cursor = null;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery("Select * from User where "+T1COL9+" = ?", new String[]{"1"});
            if (cursor.getCount() > 0)
                return cursor;
            else
                return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean insertPost(int userId, String post, byte[] image, String date, int isApproved) {
        Log.e("TAG", "image" + image);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(T2COL2, userId);
            values.put(T2COL3, post);
            values.put(T2COL4, image);
            values.put(T2COL6, date);
            values.put(T2COL7, isApproved);

            long result = db.insert(TABLE2_Name, null, values);
            if (result == -1)
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Posts> getPosts() {
        ArrayList<Posts> postsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from POST where "+T2COL7+" = ?", new String[]{"1"});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            Log.e("TAG", " cursor.getBlob(cursor.getColumnIndex(T2COL4)) " + cursor.getBlob(cursor.getColumnIndex(T2COL4)));
                            Posts post = new Posts();
                            post.id = cursor.getInt(cursor.getColumnIndex(T2COL1));
                            post.userId = cursor.getInt(cursor.getColumnIndex(T2COL2));
                            post.post = cursor.getString(cursor.getColumnIndex(T2COL3));
                            post.imgByte = cursor.getBlob(cursor.getColumnIndex(T2COL4));
                            post.date = cursor.getString(cursor.getColumnIndex(T2COL6));
                            postsList.add(post);
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public ArrayList<Posts> getMyPosts(int userId) {
        ArrayList<Posts> postsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from POST where "+T2COL2+" = ? ", new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            Posts post = new Posts();
                            post.id = cursor.getInt(cursor.getColumnIndex(T2COL1));
                            post.userId = cursor.getInt(cursor.getColumnIndex(T2COL2));
                            post.post = cursor.getString(cursor.getColumnIndex(T2COL3));
                            post.imgByte = cursor.getBlob(cursor.getColumnIndex(T2COL4));
                            post.date = cursor.getString(cursor.getColumnIndex(T2COL6));
                            post.isApproved = cursor.getInt(cursor.getColumnIndex(T2COL7));
                            postsList.add(post);
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public Posts getSinglePost(int id) {
        Posts postsList = new Posts();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from POST where "+T1COL1+" = ?", new String[]{String.valueOf(id)});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            postsList.id = cursor.getInt(cursor.getColumnIndex(T2COL1));
                            postsList.userId = cursor.getInt(cursor.getColumnIndex(T2COL2));
                            postsList.post = cursor.getString(cursor.getColumnIndex(T2COL3));
                            postsList.imgByte = cursor.getBlob(cursor.getColumnIndex(T2COL4));
                            postsList.date = cursor.getString(cursor.getColumnIndex(T2COL6));
                            postsList.isApproved = cursor.getInt(cursor.getColumnIndex(T2COL7));
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public ArrayList<Posts> getAllPosts() {
        ArrayList<Posts> postsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from POST ", new String[]{});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            Posts post = new Posts();
                            post.id = cursor.getInt(cursor.getColumnIndex(T2COL1));
                            post.userId = cursor.getInt(cursor.getColumnIndex(T2COL2));
                            post.post = cursor.getString(cursor.getColumnIndex(T2COL3));
                            post.imgByte = cursor.getBlob(cursor.getColumnIndex(T2COL4));
                            post.date = cursor.getString(cursor.getColumnIndex(T2COL6));
                            post.isApproved = cursor.getInt(cursor.getColumnIndex(T2COL7));
                            postsList.add(post);
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public Integer approvePost(int postId, int isApproved) {
        int cursor = 0;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(T2COL7, isApproved);
        cursor = MyDB.update(TABLE2_Name, newValues, "Id = " + postId, null);
        return cursor;
    }


    public Integer updatePost(int postId, String text, byte[] image) {
        int cursor = 0;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(T2COL3, text);
        newValues.put(T2COL4, image);
        cursor = MyDB.update(TABLE2_Name, newValues, "Id = " + postId, null);
        return cursor;
    }

    public ArrayList<Users> getAllUsers() {
        ArrayList<Users> postsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + TABLE1_Name + " where "+T1COL7+" != ?", new String[]{"admin"});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            Users user = new Users();
                            user.id = cursor.getInt(cursor.getColumnIndex(T1COL1));
                            user.firstname = cursor.getString(cursor.getColumnIndex(T1COL2));
                            user.lastname = cursor.getString(cursor.getColumnIndex(T1COL3));
                            user.dob = cursor.getString(cursor.getColumnIndex(T1COL4));
                            user.program = cursor.getString(cursor.getColumnIndex(T1COL6));
                            user.role = cursor.getString(cursor.getColumnIndex(T1COL7));
                            user.email = cursor.getString(cursor.getColumnIndex(T1COL8));
                            user.isApproved = cursor.getInt(cursor.getColumnIndex(T1COL10));
                            Log.e("TAG", "  user.role  " + user.role);
                            Log.e("TAG", "  user.program  " + user.program);
                            postsList.add(user);
                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    public Integer approveDisableUser(int userId, int isApproved) {
        int cursor = 0;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(T1COL10, isApproved);
        cursor = MyDB.update(TABLE1_Name, newValues, "Id = " + userId, null);
        return cursor;
    }


    public Cursor isAdminExists() {
        Cursor cursor = null;
        Calendar calendar = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(calendar.getTime());

        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery("Select * from User where "+T1COL7+" = ?", new String[]{"admin"});
            if (cursor.getCount() > 0)
                return cursor;
            else {
                insertData("admin@gmail.com", "admin", "admin", "123456", date, "", "admin", 1);

            }
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public int getPostLikes(int postId) {
        int count = 0;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + TABLE3_Name + " where "+T3COL3+" = ?", new String[]{String.valueOf(postId)});
            if (cursor.getCount() > 0)
                count = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean getLikeUnlikePost(int postId, int userId) {
        boolean count = false;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + TABLE3_Name + " where postId = ? and userId = ?", new String[]{String.valueOf(postId), String.valueOf(userId)});
            if (cursor.getCount() > 0)
                count = true;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public Users getSingleUser(int id) {
        Users user = new Users();
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + TABLE1_Name + " where Id = ?", new String[]{String.valueOf(id)});
            if (cursor != null && cursor.getCount() > 0) {
                {
                    if (cursor.moveToFirst()) {
                        do {
                            user.id = cursor.getInt(cursor.getColumnIndex(T1COL1));
                            user.firstname = cursor.getString(cursor.getColumnIndex(T1COL2));
                            user.lastname = cursor.getString(cursor.getColumnIndex(T1COL3));
                            user.email = cursor.getString(cursor.getColumnIndex(T1COL8));
                            user.dob = cursor.getString(cursor.getColumnIndex(T1COL4));
                            user.program = cursor.getString(cursor.getColumnIndex(T1COL6));
                            user.role = cursor.getString(cursor.getColumnIndex(T1COL7));
                            user.isApproved = cursor.getInt(cursor.getColumnIndex(T1COL10));

                        } while (cursor.moveToNext());
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean unlikePost(int postId, int userId) {
        boolean count = false;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            int i = sqLiteDatabase.delete(TABLE3_Name, "postId = ? and userId = ?", new String[]{String.valueOf(postId), String.valueOf(userId)});
            if (i > 0) {
                count = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean likePost(int postId, int userId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(T3COL2, userId);
            values.put(T3COL3, postId);
            long result = db.insert(TABLE3_Name, null, values);
            if (result == -1)
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public void alertTable() {
        boolean count = false;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("ALTER " + TABLE1_Name + " add isLogin = INTEGER ", null);
            if (cursor.getCount() > 0)
                count = true;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

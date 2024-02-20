package com.example.plantzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_PASSWORD + " TEXT)";

    private static final String SQL_CREATE_POSTS_TABLE =
            "CREATE TABLE " + DBContract.PostEntry.TABLE_NAME + " (" +
                    DBContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.PostEntry.COLUMN_DESCRIPTION + " TEXT," +
                    DBContract.PostEntry.COLUMN_IMAGE_URI + " TEXT)";


    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_POSTS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.PostEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, "login.db", null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        // Create the Users table
        db.execSQL(SQL_CREATE_USERS_TABLE);

        // Create the Posts table
        db.execSQL(SQL_CREATE_POSTS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL(SQL_DELETE_USERS_TABLE);
        db.execSQL(SQL_DELETE_POSTS_TABLE);

        // Create new tables
        onCreate(db);
    }

    // Your existing methods for user-related operations...

    public Boolean insertPostData(String description, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBContract.PostEntry.COLUMN_DESCRIPTION, description);
        values.put(DBContract.PostEntry.COLUMN_IMAGE_URI, imageUri);

        long result = db.insert(DBContract.PostEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    // Your existing methods for post-related operations...
    public Boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        if (result == -1) return false;
        else return true;
    }



    public boolean checkusername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean checkuserpassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

}

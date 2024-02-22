package com.example.plantzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";
    private static final int DATABASE_VERSION = 3; // Incremented the version number

    // SQL statements for creating tables
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_PASSWORD + " TEXT)";

    private static final String SQL_CREATE_POSTS_TABLE =
            "CREATE TABLE " + DBContract.PostEntry.TABLE_NAME + " (" +
                    DBContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.PostEntry.COLUMN_DESCRIPTION + " TEXT," +
                    DBContract.PostEntry.COLUMN_IMAGE_URI + " TEXT)";

    private static final String SQL_CREATE_FEEDBACK_TABLE =
            "CREATE TABLE " + DBContract.FeedbackEntry.TABLE_NAME + " (" +
                    DBContract.FeedbackEntry.COLUMN_NAME + " TEXT," +
                    DBContract.FeedbackEntry.COLUMN_FEEDBACK + " TEXT)";


    // SQL statements for dropping tables
    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_POSTS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.PostEntry.TABLE_NAME;

    private static final String SQL_DELETE_FEEDBACK_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.FeedbackEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the Users table
        db.execSQL(SQL_CREATE_USERS_TABLE);

        // Create the Posts table
        db.execSQL(SQL_CREATE_POSTS_TABLE);

        // Create the Feedback table
        db.execSQL(SQL_CREATE_FEEDBACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades for different versions
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            switch (version) {
                case 2:
                    // Upgrade logic for version 2
                    // Example: db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + newColumn + " INTEGER");
                    break;
                // Add more cases for other versions as needed
            }
        }

        // Drop existing tables if they exist
        db.execSQL(SQL_DELETE_USERS_TABLE);
        db.execSQL(SQL_DELETE_POSTS_TABLE);
        db.execSQL(SQL_DELETE_FEEDBACK_TABLE);

        // Create new tables
        onCreate(db);
    }

    // Insert feedback data into the Feedback table
    public Boolean feedData(String name, String feedback) {
        Log.d("DBHelper", "Inserting feedback - Name: " + name + ", Feedback: " + feedback);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(DBContract.FeedbackEntry.COLUMN_NAME, name);
            values.put(DBContract.FeedbackEntry.COLUMN_FEEDBACK, feedback);

            long result = db.insert(DBContract.FeedbackEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting feedback: " + e.getMessage());
            return false;
        }
    }

    // Your existing methods for user-related operations...

    // Insert user data into the Users table
    public Boolean insertData(String username, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(DBContract.UserEntry.COLUMN_USERNAME, username);
            values.put(DBContract.UserEntry.COLUMN_PASSWORD, password);

            long result = db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user data: " + e.getMessage());
            return false;
        }
    }

    // Your existing methods for post-related operations...

    // Check if a username already exists in the Users table
    public boolean checkusername(String username) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.UserEntry.TABLE_NAME +
                     " WHERE " + DBContract.UserEntry.COLUMN_USERNAME + "=?", new String[]{username})) {

            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DBHelper", "Error checking username: " + e.getMessage());
            return false;
        }
    }

    // Check if a username and password match in the Users table
    public boolean checkuserpassword(String username, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.UserEntry.TABLE_NAME +
                     " WHERE " + DBContract.UserEntry.COLUMN_USERNAME + "=? AND " +
                     DBContract.UserEntry.COLUMN_PASSWORD + "=?", new String[]{username, password})) {

            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DBHelper", "Error checking user password: " + e.getMessage());
            return false;
        }
    }
}

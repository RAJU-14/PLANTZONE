package com.example.plantzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static com.example.plantzone.DBContract.PostEntry;
import static com.example.plantzone.DBContract.UserEntry;
import static com.example.plantzone.DBContract.FeedbackEntry;
import static com.example.plantzone.DBContract.CommentEntry;
import static com.example.plantzone.DBContract.ImagesEntry;
public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";
    private static final int DATABASE_VERSION = 11;

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_EMAIL = "email";

    private String email;

    public String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        // Log the retrieved email
        Log.d("DBHelper", "Retrieved email from SharedPreferences: " + email);

        return email;
    }





    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_EMAIL + " VARCHAR(30) PRIMARY KEY," + // Making email primary key
                    UserEntry.COLUMN_PHONE + " BIGINT," +
                    UserEntry.COLUMN_USERNAME + " VARCHAR(30)," +
                    UserEntry.COLUMN_PASSWORD + " VARCHAR(30)," +
                    UserEntry.COLUMN_DOB + " VARCHAR(30))";



    private static final String SQL_CREATE_POSTS_TABLE =
            "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +
                    PostEntry.COLUMN_EMAIL + " VARCHAR(30)," +
                    PostEntry.COLUMN_DESCRIPTION + " VARCHAR(250)," +
                    PostEntry.COLUMN_IMAGE_URI + " TEXT," +
                    PostEntry.COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," + // Add column for date and time
                    "FOREIGN KEY(" + PostEntry.COLUMN_EMAIL + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_EMAIL + ") ON DELETE CASCADE)";



    private static final String SQL_CREATE_FEEDBACK_TABLE =
            "CREATE TABLE " + FeedbackEntry.TABLE_NAME + " (" +
                    FeedbackEntry.COLUMN_NAME + " TEXT," +
                    FeedbackEntry.COLUMN_FEEDBACK + " TEXT," +
                    FeedbackEntry.COLUMN_USER_EMAIL + " VARCHAR(30)," +
                    "FOREIGN KEY(" + FeedbackEntry.COLUMN_USER_EMAIL + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_EMAIL + "))";

    private static final String SQL_CREATE_COMMENTS_TABLE =
            "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                    CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Add an auto-increment primary key
                    CommentEntry.COLUMN_EMAIL + " VARCHAR(30)," +
                    CommentEntry.COLUMN_COMMENT + " VARCHAR(250)," +
                    CommentEntry.COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," + // Add column for date and time
                    "FOREIGN KEY(" + CommentEntry.COLUMN_EMAIL + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_EMAIL + ") ON DELETE CASCADE)";

    private static final String SQL_CREATE_IMAGES_TABLE =
            "CREATE TABLE " + ImagesEntry.TABLE_IMAGES + " (" +
                    ImagesEntry.COLUMN_EMAIL + " VARCHAR(30)," + // Change from COLUMN_ID to COLUMN_EMAIL
                    ImagesEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                    ImagesEntry.COLUMN_PREDICTION + " TEXT NOT NULL," +
                    "FOREIGN KEY(" + ImagesEntry.COLUMN_EMAIL + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_EMAIL + ") ON DELETE CASCADE)";



    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_POSTS_TABLE =
            "DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME;

    private static final String SQL_DELETE_FEEDBACK_TABLE =
            "DROP TABLE IF EXISTS " + FeedbackEntry.TABLE_NAME;
    private static final String SQL_DELETE_COMMENTS_TABLE  =
            "DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME;
    private static final String SQL_DELETE_IMAGES_TABLE  =
            "DROP TABLE IF EXISTS " + ImagesEntry.TABLE_IMAGES;
    public DBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_POSTS_TABLE);
        db.execSQL(SQL_CREATE_FEEDBACK_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
        db.execSQL(SQL_CREATE_IMAGES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            switch (version) {
                case 2:
                    // Upgrade logic for version 2
                    break;
                case 3:
                    // Upgrade logic for version 3
                    break;
                case 4:
                    // Upgrade logic for version 4: Add comments column to posts table

                    break;
                // Add more cases for additional upgrades if needed

            }
        }
        // Drop existing tables and recreate them
        db.execSQL(SQL_DELETE_USERS_TABLE);
        db.execSQL(SQL_DELETE_POSTS_TABLE);
        db.execSQL(SQL_DELETE_FEEDBACK_TABLE);
        db.execSQL(SQL_DELETE_COMMENTS_TABLE);
        db.execSQL(SQL_DELETE_IMAGES_TABLE);
        onCreate(db);

    }

    public boolean insertData(String email, String username, String password, String phone, String dob) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(UserEntry.COLUMN_EMAIL, email);
            values.put(UserEntry.COLUMN_USERNAME, username);
            values.put(UserEntry.COLUMN_PASSWORD, password);
            values.put(UserEntry.COLUMN_PHONE, phone); // Add phone number
            values.put(UserEntry.COLUMN_DOB, dob); // Add date of birth

            long result = db.insert(UserEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user data: " + e.getMessage());
            return false;
        }
    }

    public long insertImage(byte[] image, String prediction, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtain a writable database instance
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(ImagesEntry.COLUMN_IMAGE, image);
        values.put(ImagesEntry.COLUMN_PREDICTION, prediction);
        return db.insert(ImagesEntry.TABLE_IMAGES, null, values);
    }

    public long storeImageInDatabase(Bitmap capturedImage, String predictionResult, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(ImagesEntry.COLUMN_IMAGE, imageBytes);
        values.put(ImagesEntry.COLUMN_PREDICTION, predictionResult);

        return db.insert(ImagesEntry.TABLE_IMAGES, null, values);
    }

    public boolean addPost(String description, String imageUri, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_EMAIL, email);
        values.put(PostEntry.COLUMN_DESCRIPTION, description);
        values.put(PostEntry.COLUMN_IMAGE_URI, imageUri);

        // Adding current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = sdf.format(new Date()); // Get current date and time
        values.put(PostEntry.COLUMN_DATE_TIME, formattedDateTime);

        long result = db.insert(PostEntry.TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public Boolean feedData(String name, String feedback,String email) {
        Log.d("DBHelper", "Inserting feedback - Name: " + name + ", Feedback: " + feedback + ", User_email: " + email);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(DBContract.FeedbackEntry.COLUMN_NAME, name);
            values.put(DBContract.FeedbackEntry.COLUMN_FEEDBACK, feedback);
            values.put(FeedbackEntry.COLUMN_USER_EMAIL, email);
            long result = db.insert(DBContract.FeedbackEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting feedback: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PostEntry.COLUMN_EMAIL + ", " +
                PostEntry.COLUMN_DESCRIPTION + ", " +
                PostEntry.COLUMN_IMAGE_URI + ", " +
                PostEntry.COLUMN_DATE_TIME + // Include date and time column
                " FROM " + PostEntry.TABLE_NAME, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int emailIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_EMAIL); // Fetch email column index
                int descriptionIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_DESCRIPTION);
                int imageUriIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_IMAGE_URI);
                int dateTimeIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_DATE_TIME); // Fetch date and time column index

                String email = cursor.getString(emailIndex); // Get email from cursor
                String description = cursor.getString(descriptionIndex);
                String imageUriString = cursor.getString(imageUriIndex);
                Uri imageUri = Uri.parse(imageUriString);

                // Parse date and time from the cursor
                String dateTimeString = cursor.getString(dateTimeIndex);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dateTime = null;
                try {
                    dateTime = sdf.parse(dateTimeString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Since id is not available from the query, set it to -1
                posts.add(new Post(-1, description, imageUri, email, dateTime)); // Create Post object and add to list
            }
            cursor.close();
        }
        return posts;
    }




    public boolean addComment(int postId, String comment, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the email value in ContentValues
        values.put(UserEntry.COLUMN_EMAIL, email);

        // Set the comment value in ContentValues
        values.put(CommentEntry.COLUMN_COMMENT, comment);

        // Insert current date and time along with the comment
        values.put(CommentEntry.COLUMN_DATE_TIME, getCurrentDateTime());

        try {
            // Insert the values into the database
            long result = db.insertOrThrow(CommentEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding comment: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Method to get current date and time in string format
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    public boolean checkusername(String username) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME +
                     " WHERE " + UserEntry.COLUMN_USERNAME + "=?", new String[]{username})) {

            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DBHelper", "Error checking username: " + e.getMessage());
            return false;
        }
    }


    public boolean checkuserpassword(String username, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(UserEntry.TABLE_NAME, null, UserEntry.COLUMN_USERNAME + "=? AND " + UserEntry.COLUMN_PASSWORD + "=?", new String[]{username, password}, null, null, null)) {

            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DBHelper", "Error checking user password: " + e.getMessage());
            return false;
        }
    }
}

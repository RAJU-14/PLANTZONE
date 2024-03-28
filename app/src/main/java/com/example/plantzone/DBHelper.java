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
    private static final int DATABASE_VERSION = 15;

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
                    UserEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Change to AUTOINCREMENT
                    UserEntry.COLUMN_USERNAME + " TEXT," +
                    UserEntry.COLUMN_EMAIL + " TEXT," +
                    UserEntry.COLUMN_PHONE + " TEXT," +
                    UserEntry.COLUMN_DOB + " TEXT," +
                    UserEntry.COLUMN_USER_TYPE + " TEXT," +
                    UserEntry.COLUMN_PASSWORD + " TEXT)";




    private static final String SQL_CREATE_POSTS_TABLE =
            "CREATE TABLE " + DBContract.PostEntry.TABLE_NAME + " (" +
                    DBContract.PostEntry.COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.PostEntry.COLUMN_USER_ID + " INTEGER," +
                    DBContract.PostEntry.COLUMN_DESCRIPTION + " TEXT," +
                    DBContract.PostEntry.COLUMN_IMAGE_URI + " TEXT," +
                    DBContract.PostEntry.COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(" + DBContract.PostEntry.COLUMN_USER_ID + ") REFERENCES " +
                    DBContract.UserEntry.TABLE_NAME + "(" + DBContract.UserEntry.COLUMN_USER_ID + ") ON DELETE CASCADE)";

    private static final String SQL_CREATE_FEEDBACK_TABLE =
            "CREATE TABLE " + FeedbackEntry.TABLE_NAME + " (" +
                    FeedbackEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY," + // Use COLUMN_USER_ID as primary key
                    FeedbackEntry.COLUMN_NAME + " TEXT," +
                    FeedbackEntry.COLUMN_FEEDBACK + " TEXT," +
                    FeedbackEntry.COLUMN_DATE_TIME + " TEXT," +
                    "FOREIGN KEY(" + FeedbackEntry.COLUMN_USER_ID + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_USER_ID + "))";


    private static final String SQL_CREATE_COMMENTS_TABLE =
            "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                    CommentEntry.COLUMN_USER_ID + " INTEGER," +
                    CommentEntry.COLUMN_POST_ID + " INTEGER," +
                    CommentEntry.COLUMN_COMMENT + " TEXT," +
                    CommentEntry.COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(" + CommentEntry.COLUMN_USER_ID + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_USER_ID + ") ON DELETE CASCADE," +
                    "FOREIGN KEY(" + CommentEntry.COLUMN_POST_ID + ") REFERENCES " +
                    PostEntry.TABLE_NAME + "(" + PostEntry.COLUMN_POST_ID + ") ON DELETE CASCADE)";


    private static final String SQL_CREATE_IMAGES_TABLE =
            "CREATE TABLE " + ImagesEntry.TABLE_IMAGES + " (" +
                    ImagesEntry.COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ImagesEntry.COLUMN_USER_ID + " INTEGER," +
                    ImagesEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                    ImagesEntry.COLUMN_PREDICTION + " TEXT NOT NULL," +
                    "FOREIGN KEY(" + ImagesEntry.COLUMN_USER_ID + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_USER_ID + ") ON DELETE CASCADE)";



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

    public boolean insertData(String username, String password, String phone, String dob, String userType,String email) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(UserEntry.COLUMN_USERNAME, username);
            values.put(UserEntry.COLUMN_EMAIL, email);
            values.put(UserEntry.COLUMN_PASSWORD, password);
            values.put(UserEntry.COLUMN_PHONE, phone); // Add phone number
            values.put(UserEntry.COLUMN_DOB, dob); // Add date of birth
            values.put(UserEntry.COLUMN_USER_TYPE, userType); // Add user type

            long result = db.insert(UserEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user data: " + e.getMessage());
            return false;
        }
    }




    public long insertImage(int postId, byte[] image, String prediction, String email,int img) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtain a writable database instance
        ContentValues values = new ContentValues();
        values.put(ImagesEntry.COLUMN_USER_ID, getUserIdByEmail(email)); // Assuming you have a method to get user ID by email
        values.put(ImagesEntry.COLUMN_IMAGE_ID, img); // Insert the post ID
        values.put(ImagesEntry.COLUMN_IMAGE, image);
        values.put(ImagesEntry.COLUMN_PREDICTION, prediction);
        return db.insert(ImagesEntry.TABLE_IMAGES, null, values);
    }


    public long storeImageInDatabase(Bitmap capturedImage, String predictionResult, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        values.put(ImagesEntry.COLUMN_USER_ID, userId); // Store the user ID
        values.put(ImagesEntry.COLUMN_IMAGE, imageBytes);
        values.put(ImagesEntry.COLUMN_PREDICTION, predictionResult);

        return db.insert(ImagesEntry.TABLE_IMAGES, null, values);
    }


    public boolean addPost(String description, String imageUri, long userId) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.PostEntry.COLUMN_USER_ID, userId);
            values.put(DBContract.PostEntry.COLUMN_DESCRIPTION, description);
            values.put(DBContract.PostEntry.COLUMN_IMAGE_URI, imageUri);

            // Adding current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDateTime = sdf.format(new Date()); // Get current date and time
            values.put(DBContract.PostEntry.COLUMN_DATE_TIME, formattedDateTime);

            // Perform the database insertion
            result = db.insert(DBContract.PostEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting post: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close(); // Close the database connection
            }
        }

        return result != -1;
    }

// No change in the getUserIdByEmail method

    // Change the access modifier of the method to public or protected
    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = -1; // Default value if user ID is not found

        // Define the projection that specifies the columns from the table to return
        String[] projection = {UserEntry.COLUMN_USER_ID};

        // Filter results WHERE "email" = email
        String selection = UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = null; // Initialize cursor
        try {
            // Query the database
            cursor = db.query(
                    UserEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null                    // don't sort the order
            );

            // Check if cursor has results
            if (cursor != null && cursor.moveToNext()) {
                // Retrieve the user ID from the cursor
                userId = cursor.getLong(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_USER_ID));
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error retrieving user ID by email: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor
            }
            db.close(); // Close the database connection
        }

        return userId;
    }

    public boolean feedData(String name, String feedback, long userId,Date dateTime) {
        Log.d("DBHelper", "Inserting feedback - Name: " + name + ", Feedback: " + feedback + ", User_ID: " + userId);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(FeedbackEntry.COLUMN_NAME, name);
            values.put(FeedbackEntry.COLUMN_FEEDBACK, feedback);
            values.put(FeedbackEntry.COLUMN_USER_ID, userId); // Use the provided user ID
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateTimeStr = sdf.format(dateTime);
            Log.d("DateTime", "Formatted date and time: " + dateTimeStr);
            // Get the current date and time
            String currentDateTime = getCurrentDateTime();
            values.put(FeedbackEntry.COLUMN_DATE_TIME, currentDateTime);

            long result = db.insert(FeedbackEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting feedback: " + e.getMessage());
            return false;
        }
    }

    // Method to get current date and time in string format
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                DBContract.PostEntry.COLUMN_POST_ID + ", " + // Include post ID column
                DBContract.PostEntry.COLUMN_USER_ID + ", " +
                DBContract.PostEntry.COLUMN_DESCRIPTION + ", " +
                DBContract.PostEntry.COLUMN_IMAGE_URI + ", " +
                DBContract.PostEntry.COLUMN_DATE_TIME + " FROM " +
                DBContract.PostEntry.TABLE_NAME, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int postIdIndex = cursor.getColumnIndexOrThrow(DBContract.PostEntry.COLUMN_POST_ID); // Fetch post ID column index
                int userIdIndex = cursor.getColumnIndexOrThrow(DBContract.PostEntry.COLUMN_USER_ID);
                int descriptionIndex = cursor.getColumnIndexOrThrow(DBContract.PostEntry.COLUMN_DESCRIPTION);
                int imageUriIndex = cursor.getColumnIndexOrThrow(DBContract.PostEntry.COLUMN_IMAGE_URI);
                int dateTimeIndex = cursor.getColumnIndexOrThrow(DBContract.PostEntry.COLUMN_DATE_TIME);

                int postId = cursor.getInt(postIdIndex); // Get post ID from cursor
                int userId = cursor.getInt(userIdIndex);
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

                posts.add(new Post(postId, userId, description, imageUri, dateTime)); // Create Post object and add to list
            }
            cursor.close();
        }
        return posts;
    }
    public boolean addComment(int postId, String comment, long userId, Date dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the userId value in ContentValues
        values.put(CommentEntry.COLUMN_USER_ID, userId);

        // Set the postId value in ContentValues
        values.put(CommentEntry.COLUMN_POST_ID, postId);

        // Set the comment value in ContentValues
        values.put(CommentEntry.COLUMN_COMMENT, comment);

        // Format date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTimeStr = sdf.format(dateTime);
        Log.d("DateTime", "Formatted date and time: " + dateTimeStr);
        // Store formatted date and time in ContentValues
        values.put(CommentEntry.COLUMN_DATE_TIME, dateTimeStr);

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

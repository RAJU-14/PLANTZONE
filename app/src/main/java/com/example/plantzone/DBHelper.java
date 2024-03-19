package com.example.plantzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.plantzone.DBContract.PostEntry;
import static com.example.plantzone.DBContract.UserEntry;
import static com.example.plantzone.DBContract.FeedbackEntry;
import static com.example.plantzone.DBContract.CommentEntry;
import static com.example.plantzone.DBContract.ImagesEntry;
public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "login.db";
    private static final int DATABASE_VERSION = 7;



    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                    UserEntry.COLUMN_USERNAME + " TEXT," +
                    UserEntry.COLUMN_PASSWORD + " TEXT)";


    private static final String SQL_CREATE_POSTS_TABLE =
            "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +
                    PostEntry.COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PostEntry.COLUMN_DESCRIPTION + " TEXT," +
                    PostEntry.COLUMN_IMAGE_URI + " TEXT)";


    private static final String SQL_CREATE_FEEDBACK_TABLE =
            "CREATE TABLE " + FeedbackEntry.TABLE_NAME + " (" +
                    FeedbackEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedbackEntry.COLUMN_NAME + " TEXT," +
                    FeedbackEntry.COLUMN_FEEDBACK + " TEXT," +
                    FeedbackEntry.COLUMN_USER_EMAIL + " TEXT," +
                    "FOREIGN KEY(" + FeedbackEntry.COLUMN_USER_EMAIL + ") REFERENCES " +
                    UserEntry.TABLE_NAME + "(" + UserEntry.COLUMN_EMAIL + "))";
    private static final String SQL_CREATE_COMMENTS_TABLE =
            "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                    CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CommentEntry.COLUMN_POST_ID + " INTEGER," +
                    CommentEntry.COLUMN_COMMENT + " TEXT," +
                    "FOREIGN KEY(" + CommentEntry.COLUMN_POST_ID + ") REFERENCES " +
                    PostEntry.TABLE_NAME + "(" + PostEntry.COLUMN_POST_ID + "))";

    private static final String SQL_CREATE_IMAGES_TABLE =
            "CREATE TABLE " + ImagesEntry.TABLE_IMAGES + " (" +
                    ImagesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ImagesEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                    ImagesEntry.COLUMN_PREDICTION + " TEXT NOT NULL)";


    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_POSTS_TABLE =
            "DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME;

    private static final String SQL_DELETE_FEEDBACK_TABLE =
            "DROP TABLE IF EXISTS " + FeedbackEntry.TABLE_NAME;
    private static final String SQL_DELETE_COMMENTS_TABLE  =
            "DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME;
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
        db.execSQL(SQL_DELETE_USERS_TABLE);
        db.execSQL(SQL_DELETE_POSTS_TABLE);
        db.execSQL(SQL_DELETE_FEEDBACK_TABLE);
        db.execSQL(SQL_DELETE_COMMENTS_TABLE);
        onCreate(db);
    }

    public Boolean insertData(String email, String username, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(UserEntry.COLUMN_EMAIL, email);
            values.put(UserEntry.COLUMN_USERNAME, username);
            values.put(UserEntry.COLUMN_PASSWORD, password);

            long result = db.insert(UserEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user data: " + e.getMessage());
            return false;
        }
    }

    public long insertImage(byte[] image, String prediction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImagesEntry.COLUMN_IMAGE, image);
        values.put(ImagesEntry.COLUMN_PREDICTION, prediction);
        return db.insert(ImagesEntry.TABLE_IMAGES, null, values);
    }




    public boolean addPost(String description, String imageUri, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PostEntry.COLUMN_DESCRIPTION, description);
        values.put(PostEntry.COLUMN_IMAGE_URI, imageUri);
        // We are not adding comments when inserting the post, as comments are added separately
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + PostEntry.TABLE_NAME, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_POST_ID); // Fetch post_id column index
                int descriptionIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_DESCRIPTION);
                int imageUriIndex = cursor.getColumnIndexOrThrow(PostEntry.COLUMN_IMAGE_URI);

                int id = cursor.getInt(idIndex);
                String description = cursor.getString(descriptionIndex);
                String imageUriString = cursor.getString(imageUriIndex);
                Uri imageUri = Uri.parse(imageUriString);

                posts.add(new Post(id, description, imageUri)); // Pass id to Post constructor
            }
            cursor.close();
        }
        return posts;
    }

    public boolean addComment(int postId, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.CommentEntry.COLUMN_POST_ID, postId);
        values.put(DBContract.CommentEntry.COLUMN_COMMENT, comment);

        try {
            long result = db.insertOrThrow(DBContract.CommentEntry.TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding comment: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<String> getCommentsForPost(int postId) {
        ArrayList<String> comments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.CommentEntry.TABLE_NAME,
                new String[]{DBContract.CommentEntry.COLUMN_COMMENT},
                DBContract.CommentEntry.COLUMN_POST_ID + "=?",
                new String[]{String.valueOf(postId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.CommentEntry.COLUMN_COMMENT));
                comments.add(comment);
            }
            cursor.close();
        }
        return comments;
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

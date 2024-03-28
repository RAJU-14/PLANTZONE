package com.example.plantzone;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Post {
    private int postId;
    private int userId;
    private String description;
    private Uri imageUri;
    private Date dateTime;
    private ArrayList<String> comments; // Initialize the comments ArrayList

    public Post(int postId, int userId, String description, Uri imageUri, Date dateTime) {
        this.postId = postId;
        this.userId = userId;
        this.description = description;
        this.imageUri = imageUri;
        this.dateTime = dateTime;
        this.comments = new ArrayList<>(); // Initialize the comments ArrayList in the constructor
    }

    // Getters and setters for postId, userId, description, imageUri, and dateTime

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    // Method to add a comment
    // Method to add a comment
    public void addComment(Context context, String comment, String userEmail, int postId, Date dateTime) {
        // Retrieve the user's email from SharedPreferences or any other storage mechanism
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString("email", null);
        String currentDateTime = getCurrentDateTime();
        if (TextUtils.isEmpty(storedEmail) || !isValidEmail(storedEmail)) {
            Toast.makeText(context, "Please provide a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the user ID associated with the email
        long userId = getUserIdFromEmail(context, storedEmail);

        // Check if the user ID is valid (not -1)
        if (userId != -1) {
            DBHelper dbHelper = new DBHelper(context);
            boolean isSuccess = dbHelper.addComment(postId, comment, userId, dateTime);

            if (isSuccess) {
                this.comments.add(comment); // Add comment to the ArrayList
                Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error adding comment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "User ID not found for email: " + storedEmail, Toast.LENGTH_SHORT).show();
        }
    }


    // Assume user authentication and email storage are handled elsewhere in the app

    // Example method to add a comment when the user is authenticated
    public void addCommentIfAuthenticated(Context context, String comment, int postId, Date dateTime) {
        // Retrieve the user's email from SharedPreferences or any other storage mechanism
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        // Check if the user is authenticated (email is not null)
        if (userEmail != null) {
            // User is authenticated, call addComment method with user's email
            addComment(context, comment, userEmail, postId, dateTime);
        } else {
            // User is not authenticated, handle this scenario (e.g., prompt user to log in)
            Toast.makeText(context, "Please log in to add a comment", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to validate email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    // Method to retrieve the user ID associated with the email
    private long getUserIdFromEmail(Context context, String email) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getUserIdByEmail(email);
    }

    // Method to trigger the addition of a comment (retrieve email from shared preferences)
    public void triggerAddComment(Context context, String comment, int postId, Date dateTime) {
        // Retrieve email from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        // Check if the retrieved email is not null
        if (userEmail != null) {
            // Use the retrieved email for further processing
            addComment(context, comment, userEmail, postId, dateTime);
        } else {
            // Handle the case where the email is not available
            Toast.makeText(context, "Email not found", Toast.LENGTH_SHORT).show();
        }
    }
}

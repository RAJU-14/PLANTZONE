package com.example.plantzone;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    private int id;
    private String description;
    private Uri imageUri;
    private ArrayList<String> comments;
    private String email;
    private Date dateTime; // Added date and time field

    public Post(int id, String description, Uri imageUri, String email, Date dateTime) {
        this.id = id;
        this.description = description;
        this.imageUri = imageUri;
        this.email = email;
        this.dateTime = dateTime;
    }



    // Getters and setters for fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public ArrayList<String> getComments() {
        if (comments == null) {
            comments = new ArrayList<>(); // Initialize comments if it's null
        }
        return comments;
    }

    public void addComment(Context context, String comment, String userEmail) {
        DBHelper dbHelper = new DBHelper(context);
        boolean isSuccess = dbHelper.addComment(id, comment, userEmail);

        if (isSuccess) {
            if (comments == null) {
                comments = new ArrayList<>(); // Ensure comments ArrayList is initialized
            }
            comments.add(comment); // Add comment to the ArrayList
            Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error adding comment", Toast.LENGTH_SHORT).show();
        }
    }

}

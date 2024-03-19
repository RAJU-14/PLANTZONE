package com.example.plantzone;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;

public class Post {
    private int id;
    private String description;
    private Uri imageUri;
    private ArrayList<String> comments;

    public Post(int id, String description, Uri imageUri) {
        this.id = id;
        this.description = description;
        this.imageUri = imageUri;
    }

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

    public ArrayList<String> getComments() {
        if (comments == null) {
            comments = new ArrayList<>(); // Initialize comments if it's null
        }
        return comments;
    }

    public void addComment(Context context, String comment) {
        DBHelper dbHelper = new DBHelper(context);
        boolean isSuccess = dbHelper.addComment(id, comment);
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

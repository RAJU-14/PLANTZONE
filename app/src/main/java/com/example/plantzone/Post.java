package com.example.plantzone;

import android.net.Uri;

public class Post {
    private int id;
    private String description;
    private Uri imageUri;

    public Post(int id, String description, Uri imageUri) {
        this.id = id;
        this.description = description;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
package com.example.plantzone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;

    private String email;
    private String description;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Find views
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText emailEditText = findViewById(R.id.email);
        email = emailEditText.getText().toString(); // Retrieve email string value from EditText

        // Set click listener for buttonSelectImage to navigate to Cropdetection activity
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Cropdetection.class);
                startActivity(i);
            }
        });

        // Retrieve posts from the database
        DBHelper dbHelper = new DBHelper(this);
        posts = dbHelper.getAllPosts();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set the adapter with the email string value
        postAdapter = new PostAdapter(this, posts, email);

        recyclerView.setAdapter(postAdapter);

        // Check and request permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            // Permission granted, open gallery
            openGallery();
        }

        // Set click listener for upload button
        Button buttonUploadFromGallery = findViewById(R.id.buttonUploadFromGallery);
        buttonUploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = editTextDescription.getText().toString();
                email = emailEditText.getText().toString();

                if (!description.isEmpty()) {
                    // Proceed with gallery selection if description is not empty
                    openGallery();
                } else {
                    // Show toast if description is empty
                    Toast.makeText(CommunityActivity.this, "Please enter a description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Open gallery to select image
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Image selected from gallery
            imageUri = data.getData();
            addPostToDatabase();
        }
    }

    // Add post to database
    private void addPostToDatabase() {
        if (description.isEmpty()) {
            // Show toast if description is empty
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            // Show toast if image is not selected
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the user ID from the users table in the database
        long userId = getUserIdFromDatabase();

        if (userId == -1) {
            // Show error message if user ID is not available
            Toast.makeText(this, "Error adding post: User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an instance of DBHelper
        DBHelper dbHelper = new DBHelper(this);

        // Insert the post into the database using the dbHelper instance
        boolean isSuccess = dbHelper.addPost(description, imageUri.toString(), userId);
        if (isSuccess) {
            // Show success message
            Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Show error message
            Toast.makeText(this, "Error adding post", Toast.LENGTH_SHORT).show();
        }
    }


    private long getUserIdFromDatabase() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        // Check if the email is not null
        if (userEmail != null) {
            // Retrieve the user ID associated with the email from the database
            DBHelper dbHelper = new DBHelper(this);
            return dbHelper.getUserIdByEmail(userEmail);
        } else {
            // Show error message if email is null
            Toast.makeText(this, "Error getting user ID: Email is null", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }





    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            } else {
                // Permission denied, show toast
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

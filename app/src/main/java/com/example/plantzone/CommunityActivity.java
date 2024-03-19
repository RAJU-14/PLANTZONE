package com.example.plantzone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
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

        // Initialize and set the adapter
        postAdapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(postAdapter);

        // Check and request permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            openGallery();
        }

        EditText editTextDescription = findViewById(R.id.editTextDescription);

        Button buttonUploadFromGallery = findViewById(R.id.buttonUploadFromGallery);
        buttonUploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editTextDescription.getText().toString();
                if (!description.isEmpty()) {
                    openGallery();
                } else {
                    Toast.makeText(CommunityActivity.this, "Please enter a description", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            addPostToDatabase(imageUri);
        }
    }

    private void addPostToDatabase(Uri imageUri) {
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        if (editTextDescription != null) {
            String description = editTextDescription.getText().toString();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an instance of DBHelper
            DBHelper dbHelper = new DBHelper(this);

            // Insert the post into the database using the dbHelper instance
            boolean isSuccess = dbHelper.addPost(description, imageUri.toString(), ""); // Provide a dummy value for comments
            if (isSuccess) {
                Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding post", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Description field is null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

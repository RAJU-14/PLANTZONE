package com.example.plantzone;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DBHelper dbHelper;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        dbHelper = new DBHelper(this);

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerViewPosts.setAdapter(postAdapter);

        loadPosts();

        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSelectImage.setOnClickListener(v -> requestStoragePermission());

        EditText editTextDescription = findViewById(R.id.editTextDescription);

        Button buttonUploadFromGallery = findViewById(R.id.buttonUploadFromGallery);
        buttonUploadFromGallery.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString();
            if (!description.isEmpty()) {
                requestStoragePermission();
            } else {
                Toast.makeText(CommunityActivity.this, "Please enter a description", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosts() {
        postList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.PostEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(DBContract.PostEntry._ID);
            int descriptionIndex = cursor.getColumnIndex(DBContract.PostEntry.COLUMN_DESCRIPTION);
            int imageIndex = cursor.getColumnIndex(DBContract.PostEntry.COLUMN_IMAGE_URI);

            int id = cursor.getInt(idIndex);
            String description = cursor.getString(descriptionIndex);
            String imageUriString = cursor.getString(imageIndex);
            Uri imageUri = Uri.parse(imageUriString);

            postList.add(new Post(id, description, imageUri));
        }
        cursor.close();
        db.close();
        postAdapter.notifyDataSetChanged();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(CommunityActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CommunityActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            // Permission is granted, open gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        }
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

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.PostEntry.COLUMN_DESCRIPTION, description);
            values.put(DBContract.PostEntry.COLUMN_IMAGE_URI, imageUri.toString());
            long newRowId = db.insert(DBContract.PostEntry.TABLE_NAME, null, values);

            if (newRowId != -1) {
                postList.add(new Post((int) newRowId, description, imageUri));
                postAdapter.notifyItemInserted(postList.size() - 1);
                Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding post", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } else {
            Toast.makeText(this, "Description field is null", Toast.LENGTH_SHORT).show();
        }
    }

    static class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

        private List<Post> postList;

        PostAdapter(List<Post> postList) {
            this.postList = postList;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            Post post = postList.get(position);
            holder.textViewDescription.setText(post.getDescription());
            holder.imageViewPost.setImageURI(post.getImageUri());
        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        static class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView imageViewPost;
            TextView textViewDescription;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);
                imageViewPost = itemView.findViewById(R.id.imageViewPost);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
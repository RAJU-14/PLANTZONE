package com.example.plantzone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Cropdetection extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1889;
    private static final int INPUT_IMAGE_WIDTH = 224;
    private static final int INPUT_IMAGE_HEIGHT = 224;
    private ImageView imageView;
    private TensorFlowLiteModelLoader modelLoader;
    private Classifier classifier;
    private List<String> labels;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropdetection);

        dbHelper = new DBHelper(this);

        this.imageView = findViewById(R.id.imageView);
        ImageView cameraButton = findViewById(R.id.button);
        Button galleryButton = findViewById(R.id.gallery_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    startCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        modelLoader = new TensorFlowLiteModelLoader(this);
        try {
            String modelFilename = "plants_model.tflite";
            String labelFilename = "label_mappings.txt";
            Interpreter interpreter = modelLoader.loadModel(modelFilename);
            labels = modelLoader.loadLabelList(labelFilename);

            if (!labels.isEmpty()) {
                classifier = new Classifier(interpreter, INPUT_IMAGE_WIDTH, labels);
            } else {
                Toast.makeText(this, "Label list is empty.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error loading TensorFlow Lite model.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Log.d("ImageInfo", "Image captured from camera: " + photo.getWidth() + "x" + photo.getHeight());
                imageView.setImageBitmap(photo);

                // Get the userId from your user data
                long userId = getUserIDFromUserData();

                // Call processAndPredictImage with the userId
                processAndPredictImage(photo, userId);
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Log.d("ImageInfo", "Image selected from gallery: " + photo.getWidth() + "x" + photo.getHeight());
                    imageView.setImageBitmap(photo);

                    // Get the userId from your user data
                    long userId = getUserIDFromUserData();

                    // Call processAndPredictImage with the userId
                    processAndPredictImage(photo, userId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long getUserIDFromUserData() {
        // Implement logic to retrieve user ID from your user data
        // For example, if you have a user session, you can retrieve it from there
        // Replace this with your actual implementation
        return 123; // Dummy user ID, replace with your logic to get the user ID
    }

    private void processAndPredictImage(Bitmap capturedImage, long userId) {
        List<Classifier.Recognition> recognitionList = classifier.recognizeImage(capturedImage);

        // Perform prediction here based on the recognitionList
        String predictionResult = performPrediction(recognitionList);

        long rowId = dbHelper.storeImageInDatabase(capturedImage, predictionResult, userId);
        if (rowId != -1) {
            Log.d("Cropdetection", "Image stored in database with ID: " + rowId);
        } else {
            Log.e("Cropdetection", "Failed to store image in database.");
        }

        showAlertDialogForDisease(predictionResult);
    }

    private String performPrediction(List<Classifier.Recognition> recognitionList) {
        // Placeholder variables to store the label with the highest confidence and its corresponding score
        String highestConfidenceLabel = null;
        float highestConfidenceScore = -1.0f;

        // Loop through each recognition object in the recognitionList
        for (Classifier.Recognition recognition : recognitionList) {
            // Check if the confidence score of the current recognition is higher than the previous highest score
            if (recognition.getConfidence() > highestConfidenceScore) {
                // Update the highestConfidenceLabel and highestConfidenceScore with the current recognition
                highestConfidenceLabel = recognition.getTitle();
                highestConfidenceScore = recognition.getConfidence();
            }
        }

        // Return the label with the highest confidence score as the prediction result
        return highestConfidenceLabel;
    }

    private void showAlertDialogForDisease(String label) {
        // Use a switch-case statement to handle different labels and show different dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Prediction Result");
        builder.setMessage("The predicted disease is: " + label);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


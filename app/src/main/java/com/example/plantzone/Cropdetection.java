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
            String modelFilename ="grape_disease_model.tflite";
            String labelFilename = "labels.txt";
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
                processAndPredictImage(photo);
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Log.d("ImageInfo", "Image selected from gallery: " + photo.getWidth() + "x" + photo.getHeight());
                    imageView.setImageBitmap(photo);
                    processAndPredictImage(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processAndPredictImage(Bitmap capturedImage) {
        List<Classifier.Recognition> recognitionList = classifier.recognizeImage(capturedImage);

        if (!recognitionList.isEmpty()) {
            Classifier.Recognition recognitionResult = recognitionList.get(0);
            showAlertDialogForDisease(recognitionResult.getTitle());
            storeImageInDatabase(capturedImage, recognitionResult.getTitle());
        } else {
            showDefaultAlertDialog();
        }
    }


    private void storeImageInDatabase(Bitmap capturedImage, String predictionResult) {
        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Insert image into the database
        long rowId = dbHelper.insertImage(imageBytes, predictionResult);
        if (rowId != -1) {
            Log.d("Cropdetection", "Image stored in database with ID: " + rowId);
        } else {
            Log.e("Cropdetection", "Failed to store image in database.");
        }
    }

    private void showAlertDialogForDisease(String label) {
        // Debugging: Print the value of label
        System.out.println("Label value: " + label);

        int layoutResId;
        switch (label) {
            case "Black Rot":
                layoutResId = R.layout.grape_black_rot;
                break;
            case "Leaf Blight":
                layoutResId = R.layout.grape_blight;
                break;
            case "ESCA":
                layoutResId = R.layout.grape_esca_black_measles;
                break;
            case "Healthy": // Handle case for "Healthy"
                layoutResId = R.layout.grape_healthy;
                break;
            default:
                layoutResId = R.layout.default_dialog;
                break;
        }

        // Inflate the view from the layout resource
        View view = View.inflate(this, layoutResId, null);

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set transparent background
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set onClickListener for "Got It" button
        view.findViewById(R.id.got_it_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDefaultAlertDialog() {
        View view = View.inflate(this, R.layout.default_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        view.findViewById(R.id.got_it_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    EditText nameEditText, feedbackEditText;
    Button submitButton;
    DBHelper dbHelper;
    long userId; // Assuming you have the user ID stored somewhere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Views
        nameEditText = findViewById(R.id.userEditText);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitButton = findViewById(R.id.button2);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String name = nameEditText.getText().toString();
                String feedback = feedbackEditText.getText().toString();

                // Check if any field is empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(feedback)) {
                    Toast.makeText(FeedbackActivity.this, "All Fields Required", Toast.LENGTH_LONG).show();
                } else {
                    // Insert feedback into the database
                    boolean feedbackInserted = dbHelper.feedData(name, feedback, userId, new Date());

                    if (feedbackInserted) {
                        Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful submission
                        nameEditText.setText("");
                        feedbackEditText.setText("");
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to Submit Feedback", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

        private String getCurrentDateTime () {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }

package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

public class FeedbackActivity extends AppCompatActivity {

    EditText nameEditText, feedbackEditText, emailEditText;
    Button submitButton;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Views
        nameEditText = findViewById(R.id.userEditText);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        emailEditText = findViewById(R.id.emailEditText);
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
                String email = emailEditText.getText().toString();

                // Check if any field is empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(feedback) || TextUtils.isEmpty(email)) {
                    Toast.makeText(FeedbackActivity.this, "All Fields Required", Toast.LENGTH_LONG).show();
                } else {
                    // Insert feedback into the database
                    boolean feedbackInserted = dbHelper.feedData(name, feedback,email);

                    if (feedbackInserted) {
                        Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_LONG).show();
                        // Clear EditText fields after successful submission
                        nameEditText.setText("");
                        feedbackEditText.setText("");
                        emailEditText.setText("");
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to Submit Feedback", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

public class FeedbackActivity extends AppCompatActivity {

    EditText e1, e2;
    Button b2; // Corrected: Added b2 for the submit button
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        e1 = findViewById(R.id.userEditText);
        e2 = findViewById(R.id.feedbackEditText);

        // Corrected: Changed from b2 to b1
        b2 = findViewById(R.id.button2); // Added b2 for the submit button
        DB = new DBHelper(this);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = e1.getText().toString();
                String feedback = e2.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(feedback)) {
                    Toast.makeText(FeedbackActivity.this, "All Fields Required", Toast.LENGTH_LONG).show();
                } else {
                    Boolean insert = DB.feedData(name, feedback);
                    if (insert) {
                        Toast.makeText(FeedbackActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_LONG).show();
                        // Optionally, you can clear the EditText fields after successful submission
                        e1.setText("");
                        e2.setText("");
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to Submit Feedback", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}


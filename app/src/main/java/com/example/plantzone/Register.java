package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity  implements View.OnClickListener {
    Button b1 ,b2;
    private TextInputEditText fullNameEditText, usernameEditText, passwordEditText, emailEditText;
    private Button registerButton;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize EditText fields
        fullNameEditText = findViewById(R.id.user);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.pass);
        emailEditText = findViewById(R.id.email);

        // Initialize Register button
        registerButton = findViewById(R.id.button1);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {

            registerUser();
            Intent i1 = new Intent(this, loginactivi.class);
            Toast.makeText(this, "Registered  Successfully", Toast.LENGTH_LONG).show();
            startActivity(i1);
        } else if (view.getId() == R.id.button2) {
            Intent i1 = new Intent(this, loginactivi.class);
            startActivity(i1);
        }

    }

        private void registerUser () {
            String fullName = fullNameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            // Perform validation on input fields
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create User object with the provided details
                User user = new User(fullName, username, password, email);

                // Generate a unique key for the user
                String userId = databaseReference.push().getKey();

                // Store the user details in Firebase Realtime Database under 'users' node
                databaseReference.child(userId).setValue(user);

                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
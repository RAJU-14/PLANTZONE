package com.example.plantzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword, etEmail, etPhone, etDob;
    Spinner spinnerUserType; // Declare Spinner
    Button btnRegister, btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditText fields
        etUsername = findViewById(R.id.user);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.pass);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etDob = findViewById(R.id.dob);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize Spinner
        spinnerUserType = findViewById(R.id.spinnerUserType);

        // Define array containing options
        String[] options = new String[]{"Faculty", "Student"};

        // Create ArrayAdapter and set it to the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);

        // Initialize buttons
        btnRegister = findViewById(R.id.button1);
        btnLogin = findViewById(R.id.button2);

        // Register button click listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();

        String savedEmail = sharedPreferences.getString("email", null);
        if (savedEmail != null) {
            Log.d("SharedPreferences", "Email saved successfully: " + savedEmail);
        } else {
            Log.e("SharedPreferences", "Failed to save email");
        }
    }

    private void register() {
        // Retrieve input values
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String dob = etDob.getText().toString();
        String userType = spinnerUserType.getSelectedItem().toString(); // Retrieve selected user type from Spinner

        // Validation
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob)) {
            Toast.makeText(Register.this, "All Fields Required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        // Insert data into database
        boolean isInserted = dbHelper.insertData(username, password, phone, dob, userType,email);

        if (isInserted) {
            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
            // Clear input fields
            etUsername.setText("");
            etPassword.setText("");
            etConfirmPassword.setText("");
            etEmail.setText("");
            etPhone.setText("");
            etDob.setText("");
            // Save email to SharedPreferences
            saveEmailToSharedPreferences(email);
        } else {
            Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }
}

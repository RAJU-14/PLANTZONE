package com.example.plantzone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class Register extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword, etEmail, etPhone, etDob;
    Button btnRegister, btnLogin;
    DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);
        etPhone = findViewById(R.id.phone); // Add this line to initialize the phone EditText
        etDob = findViewById(R.id.dob); // Add this line to initialize the dob EditText
        etUsername = findViewById(R.id.user);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.pass);
        etEmail = findViewById(R.id.email);

        btnRegister = findViewById(R.id.button1);
        btnLogin = findViewById(R.id.button2);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

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
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString(); // Retrieve phone number
        String dob = etDob.getText().toString(); // Retrieve date of birth

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob)) {
            Toast.makeText(Register.this, "All Fields Required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isInserted = dbHelper.insertData(email, username, password, phone, dob);
        if (isInserted) {
            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
            etUsername.setText("");
            etPassword.setText("");
            etConfirmPassword.setText("");
            etEmail.setText("");
            etPhone.setText(""); // Clear phone number field after successful registration
            etDob.setText(""); // Clear date of birth field after successful registration
            // Inside the register() method after successful registration
            saveEmailToSharedPreferences(email);

        } else {
            Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }
}

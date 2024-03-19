package com.example.plantzone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    Button btnRegister, btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

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

    private void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String email = etEmail.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(email)) {
            Toast.makeText(Register.this, "All Fields Required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isInserted = dbHelper.insertData(email, username, password);
        if (isInserted) {
            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }
}

package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import com.google.android.material.textfield.TextInputEditText;

public class loginactivi extends AppCompatActivity implements View.OnClickListener {
    Button b3,b2;
    private TextInputEditText usernameEditText, passwordEditText;
    private Button forgetPasswordButton, loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivi);
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        usernameEditText = findViewById(R.id.user);
        passwordEditText = findViewById(R.id.pass);
        forgetPasswordButton = findViewById(R.id.button1);
        loginButton = findViewById(R.id.button2);
        signUpButton = findViewById(R.id.button3);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {


            Intent i1 = new Intent(this, Register.class);
            startActivity(i1);
        }
        if (view.getId() == R.id.button2) {


            Intent i1 = new Intent(this, Dashboardactivity.class);
            Toast.makeText(this, "Login  Successfully", Toast.LENGTH_LONG).show();
            startActivity(i1);
        }
        }
    }
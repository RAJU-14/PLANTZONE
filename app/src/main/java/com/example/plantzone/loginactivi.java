package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class loginactivi extends AppCompatActivity implements View.OnClickListener {
    Button b3 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivi);
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3) {


            Intent i1 = new Intent(this, Register.class);
            Toast.makeText(this, "Login  Successfully", Toast.LENGTH_LONG).show();
            startActivity(i1);
        }
    }
}
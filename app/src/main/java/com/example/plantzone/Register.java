package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Register extends AppCompatActivity  implements View.OnClickListener {
    Button b1 ,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {


            Intent i1 = new Intent(this, loginactivi.class);
            Toast.makeText(this, "Registered  Successfully", Toast.LENGTH_LONG).show();
            startActivity(i1);
        }
        else if(view.getId() == R.id.button2){
            Intent i1 = new Intent(this, loginactivi.class);
            startActivity(i1);
        }
    }
}
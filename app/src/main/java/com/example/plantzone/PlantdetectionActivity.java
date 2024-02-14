package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PlantdetectionActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantdetection);

        b1 = (Button) findViewById(R.id.detectBtn);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.detectBtn) {
            Intent i = new Intent(this, Cropdetection.class);
            startActivity(i);
        }

    }
}
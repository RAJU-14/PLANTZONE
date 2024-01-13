package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GreatActivity extends AppCompatActivity  implements View.OnClickListener {
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_great);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {


            Intent i1 = new Intent(this, loginactivi.class);
            startActivity(i1);
        }
    }
}
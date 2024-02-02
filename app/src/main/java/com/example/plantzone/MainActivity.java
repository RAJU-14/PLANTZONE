package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Handler handler;
    Button b1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(MainActivity.this, secondactivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);

    }

    @Override
    public void onClick(View view) {
        }
}
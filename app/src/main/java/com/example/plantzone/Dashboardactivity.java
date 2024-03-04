package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class Dashboardactivity extends AppCompatActivity implements View.OnClickListener {

    ImageView b1, b2, b3, b4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardactivity);

        b1 = (ImageView) findViewById(R.id.imgbtn1);
        b2 = (ImageView) findViewById(R.id.imgbtn2);
        b3 = (ImageView) findViewById(R.id.imgbtn3);
        b4 = (ImageView) findViewById(R.id.imgbtn4);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgbtn4) {
            Intent i = new Intent(this, FeedbackActivity.class);
            startActivity(i);
        }
        if (view.getId() == R.id.imgbtn1) {
            Intent i = new Intent(this, PlantdetectionActivity.class);
            startActivity(i);
        }
        if (view.getId() == R.id.imgbtn3) {
            Intent i = new Intent(this, CommunityActivity.class);
            startActivity(i);
        }
        if (view.getId() == R.id.imgbtn2) {
            Intent i = new Intent(this, Cropdetails.class);
            startActivity(i);
        }
    }
}
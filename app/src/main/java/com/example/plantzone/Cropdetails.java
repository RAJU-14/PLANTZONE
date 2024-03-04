package com.example.plantzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Cropdetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropdetails);

        // Initialize views
        TextView cropNameTextView = findViewById(R.id.cropName);
        TextView cropScientificNameTextView = findViewById(R.id.cropScientificName);
        TextView cropDetailsTextView = findViewById(R.id.cropDetails);

        // Set grape details
        String cropName = "Grape";
        String cropScientificName = "Vitis vinifera";
        String cropDetails = "Grapes are one of the oldest cultivated plants. They are primarily grown for their fruit, which can be eaten fresh, dried to make raisins, or processed into wine. Grapes are typically grown in temperate regions and require well-drained soil and ample sunlight. They are known for their climbing habit, with vines that can reach great heights if supported.";

        // Set data to views
        cropNameTextView.setText(cropName);
        cropScientificNameTextView.setText(cropScientificName);
        cropDetailsTextView.setText(cropDetails);

        // Set up click listener for add culture floating action button
        findViewById(R.id.add_culture_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace this with the action you want to perform when the add culture button is clicked
                Intent i = new Intent(Cropdetails.this, Culture.class);
                startActivity(i);
            }
        });

        // Set up click listener for add info floating action button
        findViewById(R.id.add_info_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace this with the action you want to perform when the add info button is clicked

                Intent i = new Intent(Cropdetails.this, DetailActivity.class);
                startActivity(i);
            }
        });
    }
}

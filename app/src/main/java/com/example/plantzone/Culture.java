package com.example.plantzone;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Culture extends AppCompatActivity {

    private ImageView detailsCropImage;
    private TextView cropDetailsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture);

        // Initialize views
        detailsCropImage = findViewById(R.id.detailsCropImage);
        cropDetailsText = findViewById(R.id.cropDetailsText);

        // Set static crop details
        String cropName = "Grape";
        String cropDetails = "Grapes are one of the oldest cultivated plants. They are primarily grown for their fruit, which can be eaten fresh, dried to make raisins, or processed into wine. Grapes are typically grown in temperate regions and require well-drained soil and ample sunlight. They are known for their climbing habit, with vines that can reach great heights if supported.The cultivation of grapes is called Viticulture. The cultivation and harvesting of grapes are known as viticulture (from the Latin word for vine) or winegrowing (wine-growing). It is a branch of horticulture science.";

        // Set data to views
        setTitle(cropName);
        cropDetailsText.setText(cropDetails);
    }
}

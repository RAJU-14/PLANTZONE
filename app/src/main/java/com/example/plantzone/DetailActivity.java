package com.example.plantzone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private ImageView detailsCropImage;
    private TextView cropDetailsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Initialize views
        detailsCropImage = findViewById(R.id.detailsCropImage);
        cropDetailsText = findViewById(R.id.cropDetailsText);

        // Set static crop details
        String cropName = "Grape";
        String cropDetails = "Scientific Name: Vitis vinifera\n" +
                "Description: Grapes are woody perennial vines that belong to the Vitaceae family. They are among the oldest cultivated plants, with evidence of their cultivation dating back thousands of years. Grapes are primarily grown for their fruit, which is consumed fresh or used to make various products such as wine, juice, jam, and raisins.\n" +
                "Habitat: Grapes are typically cultivated in temperate regions around the world. They prefer well-drained soil and ample sunlight for optimal growth.\n" +
                "Growth Habit: Grapevines are climbing plants that can reach great heights if provided with suitable support. They have long, winding stems that produce tendrils, which help the plant cling to supporting structures such as trellises or wires.\n" +
                "Leaves: Grape leaves are large and lobed, with a distinct shape. They are often used in cooking, particularly in Mediterranean cuisine, to wrap food items like dolmas.\n" +
                "Fruit: The fruit of the grape plant is a berry known as a grape. Grapes come in various colors, including green, red, purple, and black, depending on the variety. They grow in clusters and contain seeds.\n" +
                "Cultivation: Grapes are propagated through cuttings or grafting onto rootstocks. They require regular pruning and training to manage vine growth and optimize fruit production. Additionally, grapevines are susceptible to various pests and diseases, requiring careful management and sometimes the use of pesticides or other control measures.\n" +
                "Uses: Grapes have numerous culinary and commercial uses. In addition to being consumed fresh, they are processed into wine, juice, vinegar, and various other products. Different grape varieties are grown for specific purposes, such as table grapes for fresh consumption and wine grapes for winemaking.";

        // Set data to views
        setTitle(cropName);
        cropDetailsText.setText(cropDetails);
    }
}
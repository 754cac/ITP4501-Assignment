package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Playloader extends AppCompatActivity {

    private int backgroundImageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playloader);
        // Initialize the backgroundContainer ImageView and retrieve the background resource ID from the intent
        ImageView backgroundContainer = findViewById(R.id.ivBG);
        backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        // Set the background image resource for the backgroundContainer ImageView
        backgroundContainer.setImageResource(backgroundImageResource);
    }

    // This method is called when the easy button is clicked and starts the EasyGame activity with the background resource
    public void easy(View v) {
        Intent i = new Intent(this, EasyGame.class);
        i.putExtra("background_resource", backgroundImageResource);
        startActivity(i);
    }
}
package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private ImageView backgroundContainer;
    private int currentIndex = 0;
    private int[] imageResources = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};
    private int backgroundImageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager databaseManager = DatabaseManager.getInstance(MainActivity.this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        backgroundContainer = findViewById(R.id.ivBG);
        backgroundImageResource = imageResources[currentIndex];
        backgroundContainer.setImageResource(backgroundImageResource);
    }

    public void play(View view) {
        Intent i = new Intent(this, Playloader.class);
        i.putExtra("background_resource", backgroundImageResource);
        startActivity(i);
    }

    public void ranking(View view) {
        Intent i = new Intent(this, DisplayRanking.class);
        i.putExtra("background_resource", backgroundImageResource);
        startActivity(i);
    }

    // This method is used to start the Record activity and pass the background resource to it.
    public void record(View view) {
        Intent i = new Intent(this, Record.class);
        i.putExtra("background_resource", backgroundImageResource);
        startActivity(i);
    }

    // This method is used to change the background image by incrementing the index and setting the new image resource.
    public void change(View view) {
        currentIndex = (currentIndex + 1) % imageResources.length;
        backgroundImageResource = imageResources[currentIndex];
        backgroundContainer.setImageResource(backgroundImageResource);
    }

    // This method is used to start the ReplayLoader activity and pass the background resource to it.
    public void replay(View view) {
        Intent i = new Intent(this, ReplayLoader.class);
        i.putExtra("background_resource", backgroundImageResource);
        startActivity(i);
    }

    // This method is used to properly close the app by destroying the adview, calling super.onDestroy(), and exiting the system.
    public void closeApp(View view) {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        System.exit(0);
    }


}
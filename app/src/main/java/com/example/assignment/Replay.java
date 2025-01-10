package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Replay extends AppCompatActivity {
    ImageView backgroundContainer;
    GridView gvReplay;
    int backgroundImageResource;
    final private int transparentColor = android.R.color.transparent;
    final int flipDelay = 1000;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);


        gvReplay = findViewById(R.id.gvMap);
        // Initialize the backgroundContainer ImageView and retrieve the background resource ID from the intent
        backgroundContainer = findViewById(R.id.ivBG);
        backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        backgroundContainer.setImageResource(backgroundImageResource);
        // Initialize the DatabaseManager and retrieve the SQLite database
        DatabaseManager databaseManager = DatabaseManager.getInstance(Replay.this);
        SQLiteDatabase database = databaseManager.getDatabase();
        // Retrieve the gameID from the intent and replay the corresponding game
        int gameID = getIntent().getIntExtra("gameID", 0);
        replayGame(gameID, database);
    }

    public void replayGame(int gameID, SQLiteDatabase database) {
        try {
            Cursor cursor = database.rawQuery("SELECT actions,drawable, grid FROM GamesLog WHERE gameID = " + gameID + ";", null);
            int columnIndexActions = cursor.getColumnIndex("actions");
            int columnIndexDrawable = cursor.getColumnIndex("drawable");
            int columnIndexGrid = cursor.getColumnIndex("grid");
            if (cursor.moveToFirst()) {
                // Retrieve the values from the cursor

                String actions = "";
                String drawable = "";
                String grid = "";
                if (columnIndexActions != -1) {
                    actions = cursor.getString(columnIndexActions);
                }
                if (columnIndexDrawable != -1) {
                    drawable = cursor.getString(columnIndexDrawable);
                }
                if (columnIndexGrid != -1) {
                    grid = cursor.getString(columnIndexGrid);
                }
                // Parse the actions string into individual pairs
                String[] actionPairs = actions.split("#");
                List<int[]> selectedPairs = new ArrayList<>();
                for (String pair : actionPairs) {
                    String[] positions = pair.split(",");
                    int firstCard = Integer.parseInt(positions[0]);
                    int secondCard = Integer.parseInt(positions[1]);
                    int[] cardPair = {firstCard, secondCard};
                    selectedPairs.add(cardPair);
                }

                // Parse the drawable string into an array of integers
                String[] drawableArray = drawable.split(",");
                int[] drawableInts = new int[drawableArray.length];
                for (int i = 0; i < drawableArray.length; i++) {
                    drawableInts[i] = Integer.parseInt(drawableArray[i]);
                }

                String[] gridArray = grid.split(",");
                int[] gridInts = new int[gridArray.length];
                for (int i = 0; i < gridArray.length; i++) {
                    gridInts[i] = Integer.parseInt(gridArray[i]);
                }


                imageAdapter = new ImageAdapter(this, gridInts, drawableInts);
                gvReplay.setAdapter(imageAdapter);
                Handler handler = new Handler();
/*
                for (int[] pair : selectedPairs) {
                    int index1 = pair[0];
                    int index2 = pair[1];
                    imageAdapter.displayCard(index1);
                    imageAdapter.getView(index1, null, null);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Delay output:", "delaying");
                        }
                    }, 2000);
                    imageAdapter.displayCard(index2);
                    imageAdapter.getView(index2, null, null);
                }*/

                for (int[] pair : selectedPairs) {
                    int index1 = pair[0];
                    int index2 = pair[1];

                    // Display the first card

                    imageAdapter.displayCard(index1);
                    //imageAdapter.getView(index1, null, null);

                    // Delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Display the second card after the delay
                            Log.d("Delay output:", "Delaying");

                            imageAdapter.displayCard(index2);
                           // imageAdapter.getView(index2, null, null);
                        }
                    }, 2000);
                }


                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleActions(List<int[]> actionPairs) {
        /*for (int[] pair : actionPairs) {
            int index1 = pair[0];
            int index2 = pair[1];
            imageAdapter.displayCard(1);

           */
        Handler handler = new Handler();
        int delay = 1000; // Delay in milliseconds (e.g., 1000ms = 1 second)

        for (int index = 0; index < imageAdapter.getCount(); index++) {
            final int currentIndex = index;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageAdapter.displayCard(currentIndex);
                }
            }, delay);
        }
    }


}
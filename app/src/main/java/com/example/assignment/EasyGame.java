package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EasyGame extends AppCompatActivity {

    private int countPair = 0;
    final int[] drawable = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4};
    int[] pos = {0, 1, 2, 3, 0, 1, 2, 3};

    GridView gridView;
    TextView tvMove;
    Button restartButton;
    Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

    private int firstSelectedPos = -1;
    private int moveCnt = 0;
    private StringBuilder posString = new StringBuilder();
    List<int[]> selectedPairs = new ArrayList<>();
    final private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (pos[position] == -1) {
                // The card is already matched or clicked before
                return;
            }
            // Ignore if the same card is selected again
            if (position == firstSelectedPos) {
                return;
            }
            if (firstSelectedPos < 0) {
                // First card selection
                firstSelectedPos = position;
                ((ImageView) view).setImageResource(drawable[pos[position]]);
            } else {
                // Second card selection
                int wrappedPosition = position % pos.length;
                ((ImageView) view).setImageResource(drawable[pos[wrappedPosition]]);

                if (pos[firstSelectedPos] != pos[position]) {
                    // Not a matching pair
                    int[] pair = {firstSelectedPos, position};
                    selectedPairs.add(pair);
                    final int prevPosition = firstSelectedPos;
                    final ImageView prevView = (ImageView) parent.getChildAt(prevPosition);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) view).setImageResource(R.drawable.hidden);
                            prevView.setImageResource(R.drawable.hidden);
                            // Reset the first selected position
                            firstSelectedPos = -1;
                            moveCnt++;
                            tvMove.setText("Moves: " + moveCnt);
                        }
                    }, 400); // Set the delay time in milliseconds as per your preference
                } else {
                    // Matching pair
                    int[] pair = {firstSelectedPos, position};
                    selectedPairs.add(pair);
                    // Set the selected cards as transparent
                    ((ImageView) view).setImageDrawable(transparentDrawable);
                    ((ImageView) parent.getChildAt(firstSelectedPos)).setImageDrawable(transparentDrawable);
                    // Mark the positions as matched (-1)
                    pos[firstSelectedPos] = -1;
                    pos[position] = -1;
                    // Increment the pair count and move count
                    countPair++;
                    moveCnt++;
                    tvMove.setText("Moves: " + moveCnt);
                    // Check if the player has found all pairs
                    checkWinningCondition();
                    // Reset the first selected position
                    firstSelectedPos = -1;
                }
            }
        }
    };

    // Set up the layout and initialize the views and variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_game);
        // Find the grid view, text view, and restart button in the layout
        gridView = findViewById(R.id.gvMap);
        tvMove = findViewById(R.id.tvMove);
        restartButton = findViewById(R.id.btnRestart);
        // Hide the restart button initially
        restartButton.setVisibility(View.INVISIBLE);
        // Create a list to shuffle the positions
        ArrayList<Integer> posList = new ArrayList<>();
        for (int i = 0; i < pos.length; i++) {
            posList.add(pos[i]);
        }
        // Shuffle the positions
        Collections.shuffle(posList);
        // Update the shuffled positions array
        for (int i = 0; i < pos.length; i++) {
            pos[i] = posList.get(i);
        }
        // Convert the positions array to a string for storage
        for (int i = 0; i < pos.length; i++) {
            if (i != 0) {
                posString.append(",");
            }
            posString.append(pos[i]);
        }
        // Set the adapter and click listener for the grid view
        ImageAdapter imageAdapter = new ImageAdapter(this,pos,drawable);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(itemClickListener);
        // Set the background image for the activity
        ImageView backgroundContainer = findViewById(R.id.ivBG);
        int backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        backgroundContainer.setImageResource(backgroundImageResource);
    }

    // Check if the player has won the game
    private void checkWinningCondition() {
        int totalPairs = drawable.length;
        if (countPair == totalPairs) {
            // Disable clicks on the grid view
            disableClicks();
            // Store the game information
            storeGameInformation(selectedPairs, drawable);
            // Display a toast message to indicate the player has won
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
            // Show the restart button and set its click listener
            restartButton.setVisibility(View.VISIBLE);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Restart the activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    // Disable clicks on the grid view items
    private void disableClicks() {
        GridView gridView = findViewById(R.id.gvMap);
        int count = gridView.getCount();
        for (int i = 0; i < count; i++) {
            View view = gridView.getChildAt(i);
            if (view != null) {
                view.setClickable(false);
            }
        }
    }

    // Store the game information in the database
    public void storeGameInformation(List<int[]> selectedPairs, int[] drawable) {
        try {
            // Get an instance of the DatabaseManager
            DatabaseManager databaseManager = DatabaseManager.getInstance(EasyGame.this);
            SQLiteDatabase database = databaseManager.getDatabase();
            // Get the current date and time
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            // Create a ContentValues object to store the game information
            ContentValues values = new ContentValues();
            values.put("playDate", currentDate);
            values.put("playTime", currentTime);
            values.put("moves", moveCnt);

            // Convert the selectedPairs list to a string in the format
            StringBuilder pairString = new StringBuilder();
            for (int i = 0; i < selectedPairs.size(); i++) {
                int[] pair = selectedPairs.get(i);
                if (i != 0) {
                    pairString.append("#");
                }
                pairString.append(pair[0]).append(",").append(pair[1]);
            }
            values.put("actions", pairString.toString());

            // Convert the drawable and pos arrays to comma-separated string formats
            StringBuilder drawableString = new StringBuilder();
            for (int i = 0; i < drawable.length; i++) {
                if (i != 0) {
                    drawableString.append(",");
                }
                drawableString.append(drawable[i]);
            }
            values.put("drawable", drawableString.toString());
            values.put("grid", posString.toString());
            // Insert the game information into the "GamesLog" table
            database.insert("GamesLog", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
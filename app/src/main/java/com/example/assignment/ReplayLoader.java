package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ReplayLoader extends AppCompatActivity {
    Spinner gameIDSpinner;
    Button replayButton;
    private ImageView backgroundContainer;
    private int backgroundImageResource;
    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_loader);
        // Set up views and retrieve selected background resource
        gameIDSpinner = findViewById(R.id.spGameSelector);
        replayButton = findViewById(R.id.btnReplay);
        backgroundContainer = findViewById(R.id.ivBG1);
        backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        backgroundContainer.setImageResource(backgroundImageResource);
        // Retrieve game IDs from database and display in spinner
        DatabaseManager databaseManager = DatabaseManager.getInstance(ReplayLoader.this);
        SQLiteDatabase database = databaseManager.getDatabase();
        Cursor cursor = database.rawQuery("SELECT gameID FROM GamesLog ORDER BY gameID DESC;", null);
        ArrayList<String> data = new ArrayList<>();
        int columnIndexID = cursor.getColumnIndex("gameID");
        int gameID = 0;
        while (cursor.moveToNext()) {
            if (columnIndexID != -1) {
                gameID = cursor.getInt(columnIndexID);
            }
            data.add(Integer.toString(gameID));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameIDSpinner.setAdapter(adapter);
        // Update selected item when a different game ID is selected
        gameIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemString = parent.getItemAtPosition(position).toString();
                selectedItem = Integer.parseInt(selectedItemString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cursor.close();
    }

    // Called when "Replay" button is clicked
    public void changeReplay(View view) {
        // Start Replay activity with selected background resource and game ID as extras
        Intent i = new Intent(this, Replay.class);
        i.putExtra("background_resource", backgroundImageResource);
        i.putExtra("gameID", selectedItem);
        startActivity(i);
    }
}
package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // Initialize the DatabaseManager and retrieve the SQLite database
        DatabaseManager databaseManager = DatabaseManager.getInstance(Record.this);
        SQLiteDatabase database = databaseManager.getDatabase();
        // Initialize the ListView for displaying the records
        ListView listView = findViewById(R.id.lvRecord);

        // Define the projection to retrieve all columns
        String[] projection = {"*"};

        // Create a list to store the records as strings
        List<String> stringList = new ArrayList<>();

        // Execute the database query and retrieve the cursor
        Cursor cursor = database.query("GamesLog", projection, null, null, null, null, "gameID DESC");

        // Get the column indexes for the columns of interest
        int columnIndexDate = cursor.getColumnIndex("playDate");
        int columnIndexTime = cursor.getColumnIndex("playTime");
        int columnIndexMoves = cursor.getColumnIndex("moves");
        int columnIndexActions = cursor.getColumnIndex("actions");
        int columnIndexDrawable = cursor.getColumnIndex("drawable");
        int columnIndexGrid = cursor.getColumnIndex("grid");
        // Temporary variables to store the values of each column in the current record
        String date = "";
        String time = "";
        int moves = 0;
        String actions = "";
        String drawable = "";
        String grid = "";
        // Iterate through the cursor and retrieve the values for each column in the current record
        while (cursor.moveToNext()) {
            // Check if the column index is valid and retrieve the corresponding value
            if (columnIndexDate != -1) {
                date = cursor.getString(columnIndexDate);
            }
            if (columnIndexTime != -1) {
                time = cursor.getString(columnIndexTime);
            }
            if (columnIndexMoves != -1) {
                moves = cursor.getInt(columnIndexMoves);
            }
            if (columnIndexActions != -1) {
                actions = cursor.getString(columnIndexActions);
            }
            if (columnIndexDrawable != -1) {
                drawable = cursor.getString(columnIndexDrawable);
            }
            if (columnIndexGrid != -1) {
                grid = cursor.getString(columnIndexGrid);
            }
            // Create a string representation of the current record and add it to the string list
            stringList.add("Date: " + date + "\nTime: " + time + "\nMoves: " + moves + " moves" +
                    "\nActions: " + actions + "\nDrawable: " + drawable + "\nGrid: " + grid);
        }
        // Create an ArrayAdapter with the string list and set it as the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(adapter);
        // Close the cursor
        cursor.close();
        // Initialize the backgroundContainer ImageView and set the background resource
        ImageView backgroundContainer = findViewById(R.id.ivBG);
        int backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        backgroundContainer.setImageResource(backgroundImageResource);
    }

}
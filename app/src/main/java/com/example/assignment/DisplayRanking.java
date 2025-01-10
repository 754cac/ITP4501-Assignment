package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DisplayRanking extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ranking);
        // Initialize views
        listView = findViewById(R.id.lvRanking);
        ImageView backgroundContainer = findViewById(R.id.ivBG);
        // Execute an asynchronous task to read the JSON data
        MyThread myThread = new MyThread(this);
        myThread.execute();

        // Retrieve the background resource from the previous activity
        int backgroundImageResource = getIntent().getIntExtra("background_resource", 0);
        backgroundContainer.setImageResource(backgroundImageResource);
    }
// Method to process the JSON data and display the ranking

    public void readJSON(String data) {
        try {
            // Parse the JSON array from the data string
            JSONArray rankArray = new JSONArray(data);
            // Create a list of JSON objects
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (int i = 0; i < rankArray.length(); i++) {
                JSONObject jsonObject = rankArray.getJSONObject(i);
                jsonObjectList.add(jsonObject);
            }
            // Sort the JSON objects based on the "Moves" field
            Collections.sort(jsonObjectList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject jsonObject1, JSONObject jsonObject2) {
                    int moves1 = jsonObject1.optInt("Moves", 0);
                    int moves2 = jsonObject2.optInt("Moves", 0);
                    return Integer.compare(moves1, moves2);
                }
            });
            // Create a list of strings to be displayed in the ListView
            List<String> stringList = new ArrayList<>();
            int place = 1;
            for (JSONObject jsonObject : jsonObjectList) {
                String name = jsonObject.optString("Name", "");
                int moves = jsonObject.optInt("Moves", 0);
                String result = "Rank " + place + ", " + name + ", " + moves + " moves";
                stringList.add(result);
                place++;
            }
            // Create an ArrayAdapter to populate the ListView with the stringList
            ArrayAdapter<String> adapter = new ArrayAdapter<>(listView.getContext(), android.R.layout.simple_list_item_1, stringList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            // Log any errors
            Log.d("hi", e.getMessage());
        }
    }
}
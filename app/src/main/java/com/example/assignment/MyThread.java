package com.example.assignment;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyThread extends AsyncTask<Void, Void, String> {
    private final DisplayRanking displayRanking;

    // Constructor to initialize the instance of the DisplayRanking activity
    public MyThread(DisplayRanking displayRanking) {
        this.displayRanking = displayRanking;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String data = "";
        try {
            // Create a URL object with the API endpoint
            URL url = new URL("https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run");
            // Setup connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            // Get the input stream from the connection and read the data
            InputStream stream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (Exception e) {
            // Log any exceptions that occur during the network request
            Log.d("hi", e.getMessage());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        // Pass the fetched JSON data to the DisplayRanking activity
        displayRanking.readJSON(result);
    }
}
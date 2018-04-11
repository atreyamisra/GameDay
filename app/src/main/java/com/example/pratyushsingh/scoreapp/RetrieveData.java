package com.example.pratyushsingh.scoreapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pratyushsingh on 3/22/18.
 */

/**
 * @author Pratyush Singh
 * RetrieveData is a backend asynchronous class that pulls various data from the API to populate the database
 */
public class RetrieveData extends AsyncTask<Void, Void, String>{
    private final String PLAYER_DATA_URL = "http://data.nba.net/10s/prod/v1/2017/players.json";
    private final String TEAM_DATA_URL = "http://data.nba.net/prod/v1/2017/teams.json";
    private String firstName;
    private String lastName;
    private int playerId;
    private String pos;
    private int teamId;

    public RetrieveData() {}

    /**
     * Main driver function that takes the JSON data from getPlayerJSON and populates the Database
     * @TODO: populate the database (lol)
     * @param NONE
     */
    public void populatePlayer() {
        String jsonStr = getPlayerJSON();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray playerData = jsonObject.getJSONObject("league").getJSONArray("standard");


        } catch (JSONException e) {
            //@TODO: introduce a better way to handle errors
            e.printStackTrace();
        }
    }

    /**
     * Helper function that retrieves the raw JSON data from the API
     * @return JSON
     */
    private String getPlayerJSON() {
        StringBuilder jsonResponse = new StringBuilder();
        try {
            URL callURL = new URL(PLAYER_DATA_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) callURL.openConnection(); //open connection to service

            try {
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    jsonResponse.append(line);
                }

                bufferedReader.close(); //close bufferedReader resources

            } finally {
                urlConnection.disconnect(); //close connection
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            
            return null;
        }

        return jsonResponse.toString();
    }


    public void populateTeam(){
        String jsonStr = getPlayerJSON();
        Log.e("hello",jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getTeamJSON() {
        StringBuilder jsonResponse = new StringBuilder();
        try {
            URL callURL = new URL(TEAM_DATA_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) callURL.openConnection(); //open connection to service

            try {
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    jsonResponse.append(line);
                }

                bufferedReader.close();

            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

        return jsonResponse.toString();
    }

    protected String doInBackground(Void... urls) {
        populatePlayer();

        return "";
    }
}

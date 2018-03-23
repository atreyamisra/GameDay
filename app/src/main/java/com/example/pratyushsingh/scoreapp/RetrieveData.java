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

//backend class that gets the data
public class RetrieveData extends AsyncTask<Void, Void, String>{
    private final String PLAYER_DATA_URL = "http://data.nba.net/10s/prod/v1/2017/players.json";
    private final String TEAM_DATA_URL = "http://data.nba.net/prod/v1/2017/teams.json";
    private String firstName;
    private String lastName;
    private int playerId;
    private String pos;
    private int teamId;

    public RetrieveData() {}

    public void populatePlayer() {
        String jsonStr = getPlayerJSON();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray playerData = jsonObject.getJSONObject("league").getJSONArray("standard");
             Log.e("MESSAGE: ", playerData.getJSONObject(0).get("firstName").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

        return "hello";
    }
}

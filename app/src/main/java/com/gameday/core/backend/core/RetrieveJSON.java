package com.gameday.core.backend.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveJSON {
    private static final String PLAYER_DATA_URL = "http://data.nba.net/10s/prod/v1/2017/players.json";
    private static final String TEAM_DATA_URL = "http://data.nba.net/prod/v1/2017/teams.json";
    private static final String PLAYER_PROFILE = "http://data.nba.net/data/10s/prod/v1/2017/players/";
    private static final String NBA = "standard";

    public static String getJSON(String url) {
        StringBuilder jsonResponse = new StringBuilder();
        try {
            URL callURL = new URL(url);
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

    public static String getNBA() {
        return NBA;
    }

    public static String getTeamDataUrl() {
        return TEAM_DATA_URL;
    }

    public static String getPlayerDataUrl() {
        return PLAYER_DATA_URL;
    }

    public static String getPlayerProfile(String playerId) {
        return PLAYER_PROFILE + playerId + "_profile.json";
    }



}

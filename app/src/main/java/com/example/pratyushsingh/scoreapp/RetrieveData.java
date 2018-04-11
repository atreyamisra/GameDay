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
import java.util.*;

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
            int n = playerData.length();
            ArrayList<PlayerAPI> players = new ArrayList<PlayerAPI>();
            for(int i=0; i<n; i++){
                String firstName= playerData.getJSONObject(i).get("firstName").toString();
                String lastName = playerData.getJSONObject(i).get("lastName").toString();
                String personId = playerData.getJSONObject(i).get("personId").toString();
                String teamId = playerData.getJSONObject(i).get("teamId").toString();
                String pos = playerData.getJSONObject(i).get("pos").toString();
                String ppg = getPlayerPpg(personId);
                PlayerAPI player = new PlayerAPI(personId, firstName, lastName, teamId, pos, "", Float.valueOf(ppg));
                players.add(player);
            }
            Collections.sort(players, new Comparator<PlayerAPI>() {
                @Override
                public int compare(PlayerAPI o1, PlayerAPI o2) {
                    return (int)(o1.getPpg() - o2.getPpg());
                }
            });
            Collections.reverse(players);
            String top20="";
            int m = 20;
            if(n<m)
                m=n;
            for(int i=0; i<m; i++){
                PlayerAPI player=players.get(i);
                top20+="\n"+player.getFirstName()+" "+player.getLastName();
            }
             Log.e("MESSAGE: ", top20);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getPlayerPpg(String personID){
        StringBuilder jsonResponse = new StringBuilder();
        try {
            URL callURL = new URL("http://data.nba.net/prod/v1/2017/players/" +personID+"_profile.json");
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

        String jsonStr= jsonResponse.toString();
        String ppg="";
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject playerData = jsonObject.getJSONObject("league").getJSONObject("standard").getJSONObject("stats").getJSONObject("latest");
            ppg = playerData.getString("ppg");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ppg;
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
        String jsonStr = getTeamJSON();
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

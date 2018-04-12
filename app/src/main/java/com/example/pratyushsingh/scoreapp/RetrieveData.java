package com.example.pratyushsingh.scoreapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class RetrieveData extends AsyncTask<Void, Void, String> {
    private final String PLAYER_DATA_URL = "http://data.nba.net/10s/prod/v1/2017/players.json";
    private final String TEAM_DATA_URL = "http://data.nba.net/prod/v1/2017/teams.json";
    private final String NBA = "standard";
    private Context context;

    public RetrieveData(Context context) {
        this.context = context;
    }

    /**
     * Main driver function that takes the JSON data from getPlayerJSON and populates the Database
     * @TODO: populate the database (lol)
     * @param
     */
    private void populatePlayer() {
        String jsonData = getPlayerJSON();
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray playerData = jsonObject.getJSONObject("league").getJSONArray(NBA);
            String firstName;
            String lastName;
            String personId;
            String teamId;

            for(int i = 0; i < playerData.length(); i++) {
                JSONObject playerJSON = playerData.getJSONObject(i);
                if(playerJSON.get("isActive").toString().equals("true")) {
                    firstName = getFirstName(playerJSON);
                    lastName = getLastName(playerJSON);
                    personId = getPersonId(playerJSON);
                    teamId = getTeamId(playerJSON);

                    storeToCache(firstName, lastName, personId, teamId);
                }

            }


        } catch (JSONException e) {
            //@TODO: introduce a better way to handle errors
            e.printStackTrace();
        }
    }

    private void storeToCache(String firstName, String lastName, String personId, String teamId) {
        try {
            String fileName = personId;
            String fileContentsTemp = "PLAYER ID: " + fileName + "\n" +
                    "FIRST NAME: " + firstName + "\n" +
                    "LAST NAME: " +  lastName + "\n" +
                    "TEAM ID: " + teamId + "\n";
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContentsTemp.getBytes());
            outputStream.close();
        } catch (Exception e) {
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


    private String getTeamId(JSONObject playerJSON) {
        String teamId;
        try {
            teamId = playerJSON.get("teamId").toString();
        } catch (IndexOutOfBoundsException e) {
            teamId = null;
        } catch (JSONException e) {
            teamId = null;
        }

        return teamId;
    }

    private String getPersonId(JSONObject playerJSON) {
        String personId;

        try {
            personId = playerJSON.get("personId").toString();
        } catch (IndexOutOfBoundsException e) {
            personId = null;
        } catch (JSONException e) {
            personId = null;
        }

        return personId;
    }

    private String getLastName(JSONObject playerJSON) {
        String lastName;

        try {
            lastName = playerJSON.get("lastName").toString();
        } catch (IndexOutOfBoundsException e) {
            lastName = null;
        } catch (JSONException e) {
            lastName = null;
        }

        return lastName;
    }

    private String getFirstName(JSONObject jsonObject) {
        String firstName;

        try {
            firstName = jsonObject.get("firstName").toString();
        } catch (IndexOutOfBoundsException e) {
            firstName = null;
        } catch (JSONException e) {
            firstName = null;
        }

        return firstName;
    }

//    private void test() {
//        String [] files = context.fileList();
//        for(int i = 0; i < files.length; i++) {
//            try {
//                FileInputStream file = context.openFileInput(files[i]);
//                int content;
//                while ((content = file.read()) != -1) {
//                    // convert to char and display it
//                    System.out.print((char) content);
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    protected String doInBackground(Void... urls) {
        populatePlayer();
        //test();

        return "";
    }
}

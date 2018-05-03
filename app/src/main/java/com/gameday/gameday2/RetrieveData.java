package com.gameday.gameday2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;


/**
 * Created by pratyushsingh on 3/22/18.
 */

/**
 * @author Pratyush Singh
 * RetrieveData is a backend asynchronous class that pulls various data from the API to populate the database
 */
public class RetrieveData extends AsyncTask<Void, Void, String> {
    private Context context;

    public RetrieveData(Context context) {
        this.context = context;
    }

    /**
     * Main driver function that takes the JSON data from getPlayerJSON and populates the Database
     * @param
     */
    private void populatePlayer() {
        String jsonData = RetrieveJSON.getJSON(RetrieveJSON.getPlayerDataUrl());
        HashSet<Player> players = new HashSet<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray playerData = jsonObject.getJSONObject("league").getJSONArray(RetrieveJSON.getNBA());
            String firstName;
            String lastName;
            String jerseyNumber;
            String pos;
            String personId;
            String teamId;
            String biography = "";
            Bitmap profilePhoto;

            for(int i = 0; i < playerData.length(); i++) {
                Player player = new Player();
                JSONObject playerJSON = playerData.getJSONObject(i);
                if(playerJSON.get("isActive").toString().equals("true")) {
                    firstName = getInformation(playerJSON, "firstName");
                    lastName = getInformation(playerJSON, "lastName");
                    personId = getInformation(playerJSON, "personId");
                    teamId = getInformation(playerJSON, "teamId");
                    jerseyNumber = getInformation(playerJSON, "jersey");
                    pos = getInformation(playerJSON, "pos");

                    player.setName(firstName, lastName);
                    player.setPlayerId(personId);
                    player.setBio(biography);
                    player.setPosition(pos);
                    player.setTeamId(teamId);
                    player.setNumber(jerseyNumber);

                    players.add(player);

                }

            }

            storeToCache(players);


        } catch (JSONException e) {
            //@TODO: introduce a better way to handle errors
            e.printStackTrace();
        }
    }




    private void populateTeams() {
        String jsonData = RetrieveJSON.getJSON(RetrieveJSON.getTeamDataUrl());
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray teamData = jsonObject.getJSONObject("league").getJSONArray(RetrieveJSON.getNBA());
            HashSet<Team> teams = new HashSet<>();

            String teamId;
            String teamName;
            String city;
            String conference;
            String division;
            String urlName;

            for(int i = 0; i < teamData.length(); i++) {
                JSONObject teamJSON = teamData.getJSONObject(i);
                Team team = new Team();

                if(teamJSON.get("isNBAFranchise").toString().equalsIgnoreCase("true")) {
                    teamId = getInformation(teamJSON, "teamId");
                    teamName = getInformation(teamJSON, "fullName");
                    city = getInformation(teamJSON, "city");
                    conference = getInformation(teamJSON, "confName");
                    division = getInformation(teamJSON, "divName");
                    urlName = getInformation(teamJSON, "urlName");

                    team.setTeamId(teamId);
                    team.setTeamName(teamName);
                    team.setCity(city);
                    team.setConference(conference);
                    team.setDivision(division);
                    team.setUrlName(urlName);

                    teams.add(team);

                }
            }

            storeTeamCache(teams);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void storeToCache(HashSet<Player> playerSet) {
        try {
            for(Player player: playerSet) {
                String fileName = player.getPlayerId();
                String fileContentsTemp = "PLAYER ID: " + player.getPlayerId() + "\n" +
                        "NAME: " + player.getName() + "\n" +
                        "JERSEY: " + player.getNumber() + "\n" +
                        "POSITION: " + player.getPosition() + "\n" +
                        "BIOGRAPHY: " + player.getBio() +  "\n" +
                        "TEAM ID: " + player.getTeamId() + "\n";

                saveToFileSystem(fileName, fileContentsTemp);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeTeamCache(HashSet<Team> teamSet) {
        try {
            for(Team team: teamSet) {
                String fileName = team.getTeamId();
                String fileContentsTemp = "TEAM ID: " + team.getTeamId() + "\n" +
                        "TEAM NAME: " + team.getTeamName() + "\n" +
                        "CITY: " + team.getCity() + "\n" +
                        "CONFERENCE: " + team.getConference() + "\n" +
                        "DIVISION: " + team.getDivision() + "\n" +
                        "URL: " + team.getUrlName() + "\n";

                saveToFileSystem(fileName, fileContentsTemp);

            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void test() {
        String [] files = context.fileList();
        for(int i = 0; i < files.length; i++) {
            try {
                FileInputStream file = context.openFileInput(files[i]);
                int content;
                while ((content = file.read()) != -1) {
                    // convert to char and display it
                    System.out.print((char) content);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private String getInformation(JSONObject JSON, String field) {
        String parameter;
        try {
            parameter = JSON.get(field).toString();
        } catch (IndexOutOfBoundsException e) {
            parameter = null;
        } catch (JSONException e) {
            parameter = null;
        }

        return parameter;
    }

    private int saveToFileSystem(String fileName, String fileContents) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();

            return -1;
        }

        return 1;
    }


    private class Player {
        private String playerId;
        private String teamId;
        private String name;
        private String position;
        private String bio;
        private String number;
        private Bitmap profilePhoto;

        private void setPlayerId(String id) {
            playerId = id;

        }

        private String getPlayerId() {
            return playerId;
        }

        private void setTeamId(String id) {
            teamId = id;
        }

        public String getTeamId() {
            return teamId;
        }

        private void setName(String firstName, String lastName) {
            firstName = firstName.trim();
            lastName = lastName.trim();

            name = firstName + " " + lastName;
        }

        public String getName() {
            return name;
        }

        private void setPosition(String position) {
            this.position = position;
        }

        public String getPosition() {
            return position;
        }

        private void setBio(String biography) {
            this.bio = biography;
        }

        public String getBio() {
            return bio;
        }

        private void setNumber(String number) {
            this.number = number;
        }

        private String getNumber() {
            return number;
        }

        public Bitmap getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(Bitmap profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

    }

    private class Team {
        private String teamId;
        private String teamName;
        private String city;
        private String conference;
        private String division;
        private String urlName;

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setConference(String conference) {
            this.conference = conference;
        }

        public String getConference() {
            return conference;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getDivision() {
            return division;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setUrlName(String urlName) {
            this.urlName = urlName;
        }

        public String getUrlName() {
            return urlName;
        }
    }

    protected String doInBackground(Void... urls) {
        System.out.println("EXECUTING");
        populatePlayer();
        //test();
        //populateTeams();


        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Done");
    }
}

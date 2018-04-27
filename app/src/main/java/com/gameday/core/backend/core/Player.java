/**
 * Written by: Pratyush Singh
 * Date: 4/22/18
 * This class contains methods related getting information about PLayers. The UI thread will call
 * wrapper functions that then query the file system and API to return the data
 */
package com.gameday.core.backend.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.gameday.core.backend.core.AsyncResponse.*;
import com.gameday.core.backend.util.*;



public class Player {
    private String name;
    private String team;
    private String biography;
    private String position;
    private String pointsPerGame;
    private Context context;
    private ArrayList<Player> topTwentyPlayers = new ArrayList<>();
    public AsyncResponse delegate;

    /**
     * @TODO: Add more modes here
     * Based on the appropriate "mode", the relevant data is retrieved, formatted, and returned to
     * the caller
     */
    public static final String MODE_TWENTY = "TWENTY";

    /**
     * Queries the file system and the API to get the top 20 players by scoring average. This method
     * is a wrapper method and uses the PlayerStatistics class to get the top 20 players and notifies
     * Main UI thread via the AsyncResponse Interface.
     * @param context
     * @TODO add more methods needed
     */
    public static void getTopTwenty(Context context) {
        this.context = context;

        PlayerStatistics playerStatistics = new PlayerStatistics(MODE_TWENTY);
        playerStatistics.execute();

    }

    /** Sets canonical name **/
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /** Gets canonical name **/
    public String getBiography() {
        return biography;
    }

    /** Sets canonical name **/
    public void setPosition(String position) {
        this.position = position;
    }

    /** Gets canonical name **/
    public String getPosition() {
        return position;
    }

    /** Sets canonical name **/
    public void setName(String name) {
        this.name = name;
    }

    /** Gets canonical name **/
    public String getName() {
        return name;
    }

    /** Sets canonical name **/
    public void setTeam(String team) {
        this.team = team;
    }

    /** Sets canonical name **/
    public String getTeam() {
        return team;
    }

    /** Gets canonical name **/
    public ArrayList<Player> getTopTwentyPlayers() {
        return topTwentyPlayers;
    }

    /** Sets canonical name **/
    public void setTopTwentyPlayers(ArrayList<Player> topTwentyPlayers) {
        this.topTwentyPlayers = topTwentyPlayers;
    }

    /** Sets canonical name **/
    public void setPointsPerGame(String pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    /** Gets canonical name **/
    public String getPointsPerGame() {
        return pointsPerGame;
    }

    /** Gets canonical name **/
    private Context getContext() {
        return context;
    }

    /**
     * AsyncTask class that queries the file system and API to returns a Player object to the UI
     */
    private class PlayerStatistics extends AsyncTask<String, Player, Player> {
        private ArrayList<Player> topTwentyPlayers = new ArrayList<>();
        private String mode;

        /**
         * Constructor for the inner class that recieves the mode which signifies what data needs to
         * be retrieved
         * @param mode
         */
        public PlayerStatistics(String mode) {
            this.mode = mode;
        }


        @Override
        /**
         * @Todo: Add specialized messages based on each mode?
         */
        protected void onPreExecute() {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        /**
         * Depending on mode passed by the caller, the appropriate function is called and after the
         * execution the result is passed back to the caller
         */
        protected Player doInBackground(String... strings) {
        if(mode.equals(Player.MODE_TWENTY)) {
            topTwentyPlayers();
        }
            return null;
        }

        @Override
        protected void onPostExecute(Player player) {
            if(mode.equals(Player.MODE_TWENTY)) {
                delegate.processFinish(topTwentyPlayers);
            }
        }

        private void test() {
            String[] files = context.fileList();
            for (int i = 0; i < files.length; i++) {
                try {
                    FileInputStream file = context.openFileInput(files[i]);
                    int content;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((content = file.read()) != -1) {
                        stringBuilder.append((char) content);
                        // convert to char and display it

                    }

                    System.out.println(stringBuilder.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @TargetApi(24)
        private void topTwentyPlayers() {
            //test();
            String[] files = getContext().fileList();
            ArrayList<Player> topTwentyQueue = new ArrayList<>();

            for(int i = 0; i < files.length; i++) {
                try {
                    FileInputStream file = context.openFileInput(files[i]);
                    StringBuilder contentsOfFileTemp = new StringBuilder();
                    int content;
                    while((content = file.read()) != -1) {
                         contentsOfFileTemp.append((char) content);

                    }

                    String contentsOfFile = contentsOfFileTemp.toString();
                    Player player = new Player();

                    String playerId = FileParser.parseID(contentsOfFile, FileParser.PLAYER_ID);
                    String name = FileParser.parseID(contentsOfFile, FileParser.PLAYER_NAME);
                    String position = FileParser.parseID(contentsOfFile, FileParser.POSITION);
                    String biography = FileParser.parseID(contentsOfFile, FileParser.BIOGRAPHY);
                    String teamID = FileParser.parseID(contentsOfFile, FileParser.TEAM_ID);
                    String teamName = getTeamName(teamID);

                    String jsonData = RetrieveJSON.getJSON(RetrieveJSON.getPlayerProfile(playerId));
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String pointsPerGame = parsePointsPerGame(jsonObject);

                    player.setBiography(biography);
                    player.setName(name);
                    player.setPosition(position);
                    player.setTeam(teamName);
                    player.setPointsPerGame(pointsPerGame);

                    topTwentyQueue.add(player);
                }

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(JSONException e) {
                    e.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }


           topTwentyQueue.sort(new Comparator<Player>() {
               @Override
               public int compare(Player a, Player b) {
                   double ppgA = Double.parseDouble(a.getPointsPerGame());
                   double ppgB = Double.parseDouble(b.getPointsPerGame());

                   return Double.compare(ppgA, ppgB);
               }
           });

            for(int i = 0; i < 20; i++) {
                Player temp = topTwentyQueue.get(i);
                topTwentyPlayers.add(temp);
            }
        }

        private String parsePointsPerGame(JSONObject playerProfileData) {
            try {
                return playerProfileData.getJSONObject("league").
                        getJSONObject(RetrieveJSON.getNBA()).getJSONObject("stats").
                        getJSONObject("latest").get("ppg").toString();
            } catch(JSONException e) {
                e.printStackTrace();

                return null;
            }


        }

        private String getTeamName(String teamId) {
            String jsonData = RetrieveJSON.getJSON(RetrieveJSON.getTeamDataUrl());
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray teamData = jsonObject.getJSONObject("league").getJSONArray(RetrieveJSON.getNBA());

                String id;
                String teamName;

                for(int i = 0; i < teamData.length(); i++) {
                    JSONObject teamJSON = teamData.getJSONObject(i);

                    if(teamJSON.get("isNBAFranchise").toString().equalsIgnoreCase("true")) {
                        id = getInformation(teamJSON, "teamId");
                        if(teamId.equalsIgnoreCase(id)) {
                            teamName = getInformation(teamJSON, "fullName");

                            return teamName;
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";

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

    }



}
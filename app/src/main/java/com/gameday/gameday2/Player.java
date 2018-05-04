/**
 * Written by: Pratyush Singh
 * Date: 4/22/18
 * This class contains methods related getting information about PLayers. The UI thread will call
 * wrapper functions that then query the file system and API to return the data
 */
package com.gameday.gameday2;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Player implements Comparable<Player>, Serializable {
    private String name;
    private String team;
    private String biography;
    private String position;
    private String pointsPerGame;
    private SerialBitmap profilePhoto;
    private Context context;
    private ArrayList<Player> topTwentyPlayers = new ArrayList<>();
    public AsyncResponse delegate;

    /**
     * Todo: Add more modes here
     * Based on the appropriate "mode", the relevant data is retrieved, formatted, and returned to
     * the caller
     */
    public static final String MODE_TWENTY = "TWENTY";

    /**
     * Queries the file system and the API to get the top 20 players by scoring average. This method
     * is a wrapper method and uses the PlayerStatistics class to get the top 20 players and notifies
     * Main UI thread via the AsyncResponse Interface.
     * param context
     * Todo: add more methods needed
     */
    public void getTopTwenty(Context context) {
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

    public SerialBitmap getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(SerialBitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
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

    /** Gets canonical name **/
    public String getTeam() {
        return team;
    }

    /** Gets canonical name **/
    @Deprecated
    public ArrayList<Player> getTopTwentyPlayers() {
        return topTwentyPlayers;
    }

    /** Sets canonical name **/
    @Deprecated
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

    @Override
    public int compareTo(Player other) {
        return (int)(Double.parseDouble(this.pointsPerGame) - Double.parseDouble(other.pointsPerGame));
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
         * @param  mode
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

        @TargetApi(24)
        /**
         * Goes through the NBA teams and retrieves the data for the top scorer for each player
         * TODO: Sort the players, I have been having trouble with the Comparator API
         */
        private void topTwentyPlayers() {
            String[] files = getContext().fileList();
            ArrayList<Player> topTwentyQueue = new ArrayList<>();
            HashSet<String> teamsVisited = new HashSet<>();

            for(int i = 0; i < files.length && topTwentyQueue.size() < 30; i++) {
                try {
                    FileInputStream file = context.openFileInput(files[i]);
                    StringBuilder contentsOfFileTemp = new StringBuilder();
                    int content;
                    while((content = file.read()) != -1) {
                        contentsOfFileTemp.append((char) content);

                    }

                    String contentsOfFile = contentsOfFileTemp.toString();
                    Player player = new Player();

                    String teamID = FileParser.parseID(contentsOfFile, FileParser.TEAM_ID);

                    //if a team hasn't been visited yet, then parse the relevant data
                    if(!teamsVisited.contains(teamID)) {
                        teamsVisited.add(teamID);
                        String teamName = getTeamName(teamID);

                        if (teamName.length() > 0) { //safety checks
                            String jsonData = RetrieveJSON.getJSON(RetrieveJSON.getTeamLeadersUrl(teamID));
                            JSONObject jsonObject = new JSONObject(jsonData);

                            String ppgLeaderStats = ppgLeaderForTeam(jsonObject);

                            if (!ppgLeaderStats.equals("NULL")) {
                                JSONObject ppgLeaderStatsJSON = new JSONObject((ppgLeaderStats));
                                String playerId = ppgLeaderStatsJSON.get("personId").toString();
                                String ppg = ppgLeaderStatsJSON.get("value").toString();

                                FileInputStream playerFile = context.openFileInput(playerId);
                                StringBuilder playerFileContents = new StringBuilder();
                                int info;
                                while ((info = playerFile.read()) != -1) {
                                    playerFileContents.append((char) info);

                                }

                                String playerFileData = playerFileContents.toString();

                                String name = FileParser.parseID(playerFileData, FileParser.PLAYER_NAME);
                                String position = FileParser.parseID(playerFileData, FileParser.POSITION);
                                // String biography = loadBio(name);

                                //create player object
                                //player.setBiography(biography);
                                player.setName(name);
                                String bio = loadBio(player.name);
                                bio = bio.replaceAll("\\[.*?\\]", "");
                                player.setBiography(bio);
                                player.setPosition(position);
                                player.setTeam(teamName);
                                player.setPointsPerGame(ppg);
                                Bitmap profilePhoto = loadImage(player);
                                player.setProfilePhoto(new SerialBitmap(profilePhoto));

                                topTwentyQueue.add(player);
                                playerFile.close();
                            }

                        }
                    }

                    file.close();
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

            Collections.sort(topTwentyQueue, Collections.reverseOrder());
            topTwentyPlayers.addAll(topTwentyQueue);
        }

        private String parsePointsPerGame(JSONObject playerProfileData) {
            try {
                JSONArray jsonArray = playerProfileData.getJSONObject("league").
                        getJSONObject(RetrieveJSON.getNBA()).getJSONObject("stats").
                        getJSONObject("latest").getJSONArray("ppg");

                if(jsonArray.length() > 0) {
                    return jsonArray.get(0).toString();
                } else {
                    return "NULL";
                }

            } catch(JSONException e) {
                e.printStackTrace();

                return null;
            }

        }

        private String ppgLeaderForTeam(JSONObject teamLeadersStats) {
            try {
                JSONArray jsonArray = teamLeadersStats.getJSONObject("league").getJSONObject(RetrieveJSON.getNBA()).
                        getJSONArray("ppg");

                if(jsonArray.length() > 0) {
                    return jsonArray.get(0).toString();
                } else {
                    return "NULL";
                }

            } catch (JSONException e) {
                e.printStackTrace();

                return "";
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

    public String loadBio(String name) throws IOException {
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + name).get();
        Elements paragraphs = doc.select(".mw-content-ltr p");
        Element firstParagraph = paragraphs.first();
        if(firstParagraph.text().contains("may refer to")) {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + name + " (basketball)").get();
            paragraphs = doc.select(".mw-content-ltr p");
            firstParagraph = paragraphs.first();
        }
        return firstParagraph.text();
    }

    public Bitmap loadImage(Player p) {
        Bitmap image = null;
        URL baseURL = null;
        try {
            baseURL = new URL("https://nba-players.herokuapp.com/players/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String player = formatPlayerName(p.getName());
        try {
            image = BitmapFactory.decodeStream((InputStream) new URL(baseURL + player).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public static String formatPlayerName(String player) {
        player = player.toLowerCase();
        String[] split = player.split(" ");
        if(split.length > 2)
            return split[split.length - 1];
        return split[1] + "/" + split[0];
    }
}
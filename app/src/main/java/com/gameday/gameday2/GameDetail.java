package com.gameday.gameday2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static com.gameday.gameday2.Player.formatPlayerName;

public class GameDetail {
    public AsyncResponse delegate;
    private static String gameId = "";
    private static String gameDate = "";
    private GameDetailResults gameDetailResults = new GameDetailResults();

    public static void getGameDetails(String _gameId, String _gameDate, Context context) {
        gameId = _gameId;
        gameDate = _gameDate;

        GameDetail gameDetail = new GameDetail();
        gameDetail.execute(context);
    }

    private void execute(Context context) {
        InnerGameDetail innerGameDetail = new InnerGameDetail(gameId, gameDate, context);
        innerGameDetail.execute();
    }

    private class InnerGameDetail extends AsyncTask<Void, Void, Void> {
        private String gameId;
        private String gameDate;
        private Context context;

        public InnerGameDetail(String gameId, String gameDate, Context context) {
            this.context = context;
            this.gameId = gameId;
            this.gameDate = gameDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String base_url = "http://data.nba.net/data/10s/prod/v1/";
            String full_url = base_url + gameDate + "/" + gameId + "_boxscore.json";
            String json = RetrieveJSON.getJSON(full_url);
            getBoxScore(json);

            return null;
        }

        private ArrayList<Player> retrieveTeamData(JSONObject jsonObject, String side) throws JSONException, IOException {
            String teamId = jsonObject.getJSONObject(side).get("teamId").toString();
            JSONObject stats = jsonObject.getJSONObject("stats").getJSONObject(side);
            String highestPoints = stats.getJSONObject("leaders").getJSONObject("points").getString("value");
            JSONArray pointLeaders = stats.getJSONObject("leaders").getJSONObject("points").getJSONArray("players");

            ArrayList<Player> players = new ArrayList<>();
            HashSet<String> ids = new HashSet<>();

            for(int i = 0; i < pointLeaders.length() && players.size() < 3; i++) {
                String playerId = pointLeaders.getString(i);
                ids.add(playerId);

                FileInputStream playerFile = context.openFileInput(playerId);
                StringBuilder playerFileContents = new StringBuilder();

                int info;
                while ((info = playerFile.read()) != -1) {
                    playerFileContents.append((char) info);

                }

                String playerFileData = playerFileContents.toString();
                String name = FileParser.parseID(playerFileData, FileParser.PLAYER_NAME);
                Bitmap image = loadImage(name);

                Player player = new Player();
                player.setProfilePhoto(image);
                player.setPointsScoredInGame(highestPoints);

                players.add(player);

            }

            if(players.size() < 2) {
                JSONArray allPlayers = jsonObject.getJSONArray("activePlayers");
                for(int i = 0; i < allPlayers.length() && players.size() < 2; i++) {
                    String tId = allPlayers.getJSONObject(i).getString("teamId");
                    if(!tId.equals(teamId)) continue;

                    String pId = allPlayers.getJSONObject(i).getString("personId");
                    FileInputStream playerFile = context.openFileInput(pId);
                    StringBuilder playerFileContents = new StringBuilder();

                    int info;
                    while ((info = playerFile.read()) != -1) {
                        playerFileContents.append((char) info);

                    }

                    String playerFileData = playerFileContents.toString();
                    String name = FileParser.parseID(playerFileData, FileParser.PLAYER_NAME);
                    Bitmap image = loadImage(name);

                    Player player = new Player();
                    player.setProfilePhoto(image);
                    player.setPointsScoredInGame(highestPoints);

                    players.add(player);


                }
            }


            return players;
        }

        public Bitmap loadImage(String name) {
            Bitmap image = null;
            URL baseURL = null;
            try {
                baseURL = new URL("https://nba-players.herokuapp.com/players/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String player = formatPlayerName(name);

            try {
                image = BitmapFactory.decodeStream((InputStream) new URL(baseURL + player).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }

        private void getBoxScore(String json) {
            ArrayList<Player> visitingPlayers = new ArrayList<>();
            ArrayList<Player> homePlayers = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(json);
                visitingPlayers = retrieveTeamData(jsonObject, "vTeam");
                homePlayers = retrieveTeamData(jsonObject, "hTeam");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getPastGames() {
            for(int i = 1; i < 7; i++) {
                String date = getPreviousDate(i);
                String json = RetrieveJSON.getJSON(url + date + "/scoreboard.json");
                parseGameData(json);

            }
        }

        private String getPreviousDate(int prevDay) {
            prevDay = prevDay * -1;
            final Calendar cal = Calendar.getInstance();

            String formattedDate = formatDate(cal.getTime());

            return formattedDate;
        }

        private String formatDate(Date date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String format = formatter.format(date);

            return format;
        }


            @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

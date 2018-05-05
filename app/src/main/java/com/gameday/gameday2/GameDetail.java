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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.gameday.gameday2.Player.formatPlayerName;

public class GameDetail {
    public GameDetailAsyncResponse delegate;
    private static String gameId = "";
    private static String gameDate = "";
    private GameDetailResults gameDetailResults = new GameDetailResults();
    private HashSet<String> playoffTeams = new HashSet<>(Arrays.asList("celtics\n" +
            "sixers\n" +
            "raptors\n" +
            "cavaliers\n" +
            "warriors\n" +
            "jazz\n" +
            "rockets\n" +
            "pelicans\n"));


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
        private ArrayList<Game> pastVGames = new ArrayList<>();
        private ArrayList<Game> pastHGames = new ArrayList<>();
        private Context context;
        private GameDetailResults gameDetailResults = new GameDetailResults();

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

        private ArrayList<Game> parseGameData(String gameJSON) {
            try {
                JSONObject jsonObject = new JSONObject(gameJSON);
                JSONArray games = jsonObject.getJSONObject("league").getJSONArray("standard");
                ArrayList<Game> pastGames = new ArrayList<>();

                for (int j = games.length() - 1; j >= 0 && pastGames.size() < 3; j--) {
                    Game game = new Game();

                    JSONObject gameData = games.getJSONObject(j);

                    String startTimeStr = gameData.getString("startTimeUTC");
                    Instant startTime = Instant.parse(startTimeStr);
                    Instant currentTime = Instant.now();
                    if (startTime.isAfter(currentTime)) continue;

                    String _gameId = gameData.getString("gameId");
                    if (gameId.equals(_gameId)) continue;

                    JSONObject playoffData = gameData.getJSONObject("playoffs");
                    String vTeamId = playoffData.getJSONObject("vTeam").getString("teamId");
                    String vTeamScore = playoffData.getJSONObject("vTeam").getString("score");

                    String hTeamId = playoffData.getJSONObject("hTeam").getString("teamId");
                    String hTeamScore = playoffData.getJSONObject("hTeam").getString("score");

                    String vTeamName = teamName(vTeamId);
                    String hTeamName = teamName(hTeamId);

                    game.setHomeTeam(hTeamName);
                    game.setVisitingTeam(vTeamName);
                    game.setGameId(_gameId);
                    game.sethTeamId(hTeamId);
                    game.setvTeamId(vTeamId);
                    game.setIsActive(false);
                    game.setPeriod("");
                    game.setHscore(hTeamScore);
                    game.setvScore(vTeamScore);

                    pastGames.add(game);

                }

                return pastGames;
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        private ArrayList<Player> retrieveTeamData(JSONObject jsonObject, String side) throws JSONException, IOException {
            String teamId = jsonObject.getJSONObject(side).get("teamId").toString();
            String teamName = getTeamName(teamId);

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
                for(int i = 0; i < allPlayers.length() && players.size() <= 2; i++) {
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

            if(side.equals("vTeam")) {
                pastVGames = getPastGames(teamName);
            }
            else {
                pastHGames = getPastGames(teamName);
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
            ArrayList<Player> visitingPlayers;
            ArrayList<Player> homePlayers;
            try {
                JSONObject jsonObject = new JSONObject(json);
                visitingPlayers = retrieveTeamData(jsonObject, "vTeam");
                homePlayers = retrieveTeamData(jsonObject, "hTeam");

                gameDetailResults.setArrayLists(visitingPlayers, homePlayers);
                gameDetailResults.setPastGames(pastVGames, pastHGames);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<Game> getPastGames(String teamName) {
            String url = "http://data.nba.net/data/10s/prod/v1/2017/teams/";
            String team = "";
            for(String t: playoffTeams) {
                t = t.toLowerCase();
                teamName = teamName.toLowerCase();

                if(teamName.contains(t)) {

                    team = t;
                    break;
                }
            }


                String json = RetrieveJSON.getJSON(url + team + "/schedule.json");

                return parseGameData(json);

        }


        private String teamName(String teamId) {
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


        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish(gameDetailResults);
        }
    }
}

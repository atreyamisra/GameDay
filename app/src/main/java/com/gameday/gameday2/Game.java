package com.gameday.gameday2;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Game {
    private static String currentDate = "";
    private String gameId = "";
    private String vTeamId = "";
    private String hTeamId = "";
    private String vScore = "";
    private String hscore = "";
    private String period = "";
    private String clock = "";
    private String homeTeam = "";
    private String visitingTeam = "";
    public AsyncResponse delegate;

    private List<Game> pastGame = new ArrayList<>();
    private List<Game> currentGame = new ArrayList<>();
    private List<Game> futureGame = new ArrayList<>();

    public static String getDate() {
        Date todayDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String format = formatter.format(todayDate);

        return format;
    }

    public static void getCurrentGame() {
        currentDate = getDate();

        Game game = new Game();
        game.execute();
    }

    public String getGameId() {
        return gameId;
    }

    public  void setGameId(String _gameId) {
        gameId = _gameId;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String _clock) {
        clock = _clock;

    }

    public String getHscore() {
        return hscore;
    }

    public void setHscore(String _hscore) {
        hscore = _hscore;
    }

    public String getvScore() {
        return vScore;
    }

    public void setvScore(String _vscore) {
        vScore = vScore;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String _period) {
        period = _period;
    }

    public String getvTeamId() {
        return vTeamId;
    }

    public void setvTeamId(String _vteamId) {
        vTeamId = _vteamId;
    }

    public String gethTeamId() {
        return hTeamId;
    }

    public void sethTeamId(String _hTeamId) {
        hTeamId = _hTeamId;
    }

    public String getHomeTeamName() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }


    private void execute() {
        GameStatistics gameStatistics = new GameStatistics();
        gameStatistics.execute();
    }

    private class GameStatistics extends AsyncTask<Void, Void, Void> {
        private String url = "http://data.nba.net/data/10s/prod/v1/";
        private String isActive = "";

        @Override
        protected Void doInBackground(Void... voids) {
            String json = RetrieveJSON.getJSON(url + Game.getDate() + "/scoreboard.json");
            parseGameData(json);
            getPastGames();


            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void parseGameData(String gameJSON) {
            try {
                JSONObject jsonObject = new JSONObject(gameJSON);
                JSONArray games = jsonObject.getJSONArray("games");
                Game game = new Game();

                for(int i = 0; i < games.length(); i++) {
                    JSONObject g = games.getJSONObject(i);
                    isActive = g.get("isGameActivated").toString();
                    String startTime = g.get("startTimeUTC").toString();
                    Instant startTimeInstant = Instant.parse(startTime);
                    Instant currentTime = Instant.now();

                    String gameId = g.get("gameId").toString();
                    String vTeamId = g.getJSONObject("vTeam").get("teamId").toString();
                    String hTeamId = g.getJSONObject("hTeam").get("teamId").toString();
                    String vScore = g.getJSONObject("vTeam").get("score").toString();
                    String hscore = g.getJSONObject("hTeam").get("score").toString();
                    String period = g.getJSONObject("period").get("current").toString();
                    String clock = g.get("clock").toString();

                    game.setGameId(gameId);
                    game.setvTeamId(vTeamId);
                    game.sethTeamId(hTeamId);
                    game.setvScore(vScore);
                    game.setHscore(hscore);
                    game.setPeriod(period);
                    game.setClock(clock);

                    String visitingTeam = teamName(vTeamId);
                    String homeTeam = teamName(hTeamId);

                    if(visitingTeam.length() > 0) {
                        game.setVisitingTeam(teamName(vTeamId));
                    } else {
                        game.setVisitingTeam(" ");
                    }

                    if(homeTeam.length() > 0) {
                        game.setHomeTeam(homeTeam);
                    } else {
                        game.setHomeTeam(" ");
                    }

                    if(Boolean.getBoolean(isActive)) {
                        currentGame.add(game);
                    } else if(currentTime.isAfter(startTimeInstant)) {
                        pastGame.add(game);
                    } else {
                        futureGame.add(game);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            ArrayList<ArrayList<Game>> gameData = new ArrayList<>();
            delegate.processFinish(gameData);
        }
    }


}

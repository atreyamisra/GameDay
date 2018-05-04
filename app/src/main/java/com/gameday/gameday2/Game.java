package com.gameday.gameday2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Game implements Serializable {
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
    private String homeTeamA = "";
    private String visitingTeamA = "";
    private SerialBitmap hTeamLogo;
    private SerialBitmap vTeamLogo;
    private boolean isActive = false;

    public static AsyncResponse delegate;

    private List<Game> totalGames = new ArrayList<>();

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
        vScore = _vscore;
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

    public String getHomeTeam() {
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getHomeTeamA() {
        return homeTeamA;
    }

    public void setHomeTeamA(String homeTeamA) {
        this.homeTeamA = homeTeamA;
    }

    public String getVisitingTeamA() {
        return visitingTeamA;
    }

    public void setVisitingTeamA(String visitingTeamA) {
        this.visitingTeamA = visitingTeamA;
    }

    public SerialBitmap gethTeamLogo() {
        return hTeamLogo;
    }

    public void sethTeamLogo(SerialBitmap hTeamLogo) {
        this.hTeamLogo = hTeamLogo;
    }

    public SerialBitmap getvTeamLogo() {
        return vTeamLogo;
    }

    public void setvTeamLogo(SerialBitmap vTeamLogo) {
        this.vTeamLogo = vTeamLogo;
    }


    private void execute() {
        GameStatistics gameStatistics = new GameStatistics();
        gameStatistics.execute();
    }

    private class GameStatistics extends AsyncTask<Void, Void, Void> {
        private String url = "http://data.nba.net/data/10s/prod/v1/";

        @SuppressLint("NewApi")
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


                for(int i = 0; i < games.length(); i++) {
                    Game game = new Game();
                    JSONObject g = games.getJSONObject(i);
                    String _isActive = g.get("isGameActivated").toString();
                    String startTime = g.get("startTimeUTC").toString();
                    Instant startTimeInstant = Instant.parse(startTime);
                    Instant currentTime = Instant.now();

                    String gameId = g.get("gameId").toString();
                    String vTeamId = g.getJSONObject("vTeam").get("teamId").toString();
                    String hTeamId = g.getJSONObject("hTeam").get("teamId").toString();
                    String vScore = g.getJSONObject("vTeam").get("score").toString();
                    String hscore = g.getJSONObject("hTeam").get("score").toString();
                    String period = "Q" + g.getJSONObject("period").get("current").toString();
                    String hTeamAb = g.getJSONObject("hTeam").get("triCode").toString();
                    String vTeamAb = g.getJSONObject("vTeam").get("triCode").toString();
                    String clock = g.get("clock").toString();

                    game.setGameId(gameId);
                    game.setvTeamId(vTeamId);
                    game.sethTeamId(hTeamId);
                    game.setvScore(vScore);
                    game.setHscore(hscore);
                    game.setPeriod(period);

                    if (clock.equals("")) {
                        game.setClock("FINAL");
                    } else {
                        game.setClock(clock);
                    }
                    game.setHomeTeamA(hTeamAb);
                    game.setVisitingTeamA(vTeamAb);
                    game.sethTeamLogo(new SerialBitmap(loadImage(hTeamAb)));
                    game.setvTeamLogo(new SerialBitmap(loadImage(vTeamAb)));
                    game.setIsActive(Boolean.getBoolean(_isActive));

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

                    if(totalGames.size() < 6 && !currentTime.isBefore(startTimeInstant)) {
                        totalGames.add(game);
                    }

                    if (totalGames.size() == 6) {
                        break;
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

        @SuppressLint("NewApi")
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

            cal.add(Calendar.DATE, prevDay);

            String formattedDate = formatDate(cal.getTime());

            return formattedDate;
        }

        private String formatDate(Date date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String format = formatter.format(date);

            return format;
        }

        public Bitmap loadImage(String abbrev) {
            Bitmap image = null;
            URL baseURL = null;
            try {
                baseURL = new URL("http://i.cdn.turner.com/nba/nba/.element/img/1.0/teamsites/logos/teamlogos_500x500/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                image = BitmapFactory.decodeStream((InputStream) new URL(baseURL + abbrev + ".png").getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayList<Game> games = new ArrayList<>(totalGames);
            delegate.processFinish(games);
        }
    }
}

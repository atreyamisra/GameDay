package com.gameday.gameday2;

import java.util.ArrayList;

public class GameDetailResults {
    private Game game = new Game();
    private ArrayList<Player> vTeamPlayers = new ArrayList<>();
    private ArrayList<Player> hTeamPlayers = new ArrayList<>();
    private ArrayList<Game> hPastGames = new ArrayList<>();
    private ArrayList<Game> vPastGames = new ArrayList<>();

    public void setArrayLists(ArrayList<Player> vTeamPlayers, ArrayList<Player> hTeamPlayers) {
        this.vTeamPlayers = vTeamPlayers;
        this.hTeamPlayers = hTeamPlayers;
    }

    public ArrayList<Player> gethTeamPlayers() {
        return hTeamPlayers;
    }

    public ArrayList<Player> getvTeamPlayers() {
        return vTeamPlayers;
    }

    public void setPastGames(ArrayList<Game> vPastGames, ArrayList<Game> hPastGames) {
        this.vPastGames = vPastGames;
        this.hPastGames = hPastGames;
    }

    public ArrayList<Game> gethPastGames() {
        return hPastGames;
    }

    public ArrayList<Game> getvPastGames() {
        return vPastGames;
    }
}

package com.example.pratyushsingh.scoreapp;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by atreyamisra on 4/5/18.
 */

public class PlayerAPI {
    public static FileInputStream getPlayerBioData(Context context, String playerId) {
        FileInputStream file = null;
        try {
            file = context.openFileInput(playerId);
        } catch (FileNotFoundException e) {
            /*
            TODO: if file is not found, then retrieve the data from the api and store it in the API
             */
            e.printStackTrace();
        }

        return file;
    }

    public static ArrayList<String> getPlayerId(Context context, String name) {
        String [] files = context.fileList();
        ArrayList<String> players = new ArrayList<>();

        for(int i = 0; i < files.length; i++) {
            try {
                FileInputStream file = context.openFileInput(files[i]);
                int content;
                while ((content = file.read()) != -1) {
                    String contentsOfFile = Character.toString((char) content);
                    String playerId = FileParser.parseID(contentsOfFile, FileParser.PLAYER_ID);
                    players.add(playerId);

                }
            } catch (FileNotFoundException e) {
                /**
                 * TODO if file is not found then query the api for player
                 */
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return players;
    }

    /**
     * TODO: Store a key-value pair in SharedPreferences for teamName and teamId so when teamName is pased I can retrieve the teamId quickly
     * @param context
     * @param playerName
     * @param teamName
     * @return
     */
    public static String getPlayerId(Context context, String playerName, String teamName) {
        String [] files  = context.fileList();

        return "";

    }

    public static String getPointsPerGame(Context context, String playerName, String teamName) {
        String playerId = getPlayerId(context, playerName, teamName);

        

    }

}
package com.example.pratyushsingh.scoreapp;


public class FileParser {
    public static final int PLAYER_ID = 0;
    public static final int TEAM_ID = 0;

    public static String parseID(String contentsOfFile, int itemToParse) {
        if(itemToParse == PLAYER_ID) {
            String playerID = getPlayerId(contentsOfFile);

            return playerID;
        }

        return "";
    }

    private static String getPlayerId(String contentsOfFile) {
        String [] playerData = contentsOfFile.split("\n");
        String id = "";

        for(int i = 0; i < playerData.length; i++) {
            String [] line = playerData[i].split(":");
            if(line[0].equals("PLAYER ID")) {
                try {
                    id = line[0].trim();
                    return id;

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    /**
                     * TODO: How to best handle error when there is not player id available?
                     */
                }

            }
        }

        return id;
    }
}

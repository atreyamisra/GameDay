package com.gameday.core.backend.util;


public class FileParser {
    public static final int PLAYER_ID = 0;
    public static final int TEAM_ID = 1;
    public static final int PLAYER_NAME = 2;
    public static final int POSITION = 3;
    public static final int BIOGRAPHY = 4;

    public static String parseID(String contentsOfFile, int itemToParse) {
        String  result = "";
        if(itemToParse == PLAYER_ID) {
            String playerID = getID(contentsOfFile, "PLAYER ID");
            result = playerID;

        } else if(itemToParse == TEAM_ID) {
            String teamID = getID(contentsOfFile, "TEAM ID");
            result = teamID;
        } else if(itemToParse == PLAYER_NAME) {
            String playerName = getID(contentsOfFile, "NAME");
            result = playerName;
        } else if(itemToParse == POSITION) {
            String position = getID(contentsOfFile, "POSITION");
            result = position;
        } else if(itemToParse == BIOGRAPHY) {
            String biography = getID(contentsOfFile, "BIOGRAPHY");
            result = biography;
        }

        return result;
    }

    private static String getID(String contentsOfFile, String key) {
        String[] playerData = contentsOfFile.split("\n");
        String id = "";

        for (int i = 0; i < playerData.length; i++) {
            String[] line = playerData[i].split(":");
            if (line[0].equals(key)) {
                try {
                    id = line[1].trim();

                    return id;

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    break;
                    /**
                     * TODO: How to best handle error when there is not player id available?
                     */
                }

            }
        }

        return id;
    }



}
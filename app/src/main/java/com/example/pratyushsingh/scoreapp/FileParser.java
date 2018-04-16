package com.example.pratyushsingh.scoreapp;


public class FileParser {
    public static final int PLAYER_ID = 0;
    public static final int TEAM_ID = 0;

    public static String parseID(String contentsOfFile, int itemToParse) {
        String playerID = getID(contentsOfFile, "PLAYER ID: ");
        String teamID = getID(contentsOfFile, "TEAM ID: ");


        return "";
    }

    private static String getID(String contentsOfFile, String key) {
        String[] playerData = contentsOfFile.split("\n");
        String id = "";

        for (int i = 0; i < playerData.length; i++) {
            String[] line = playerData[i].split(":");
            if (line[0].equals(key)) {
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
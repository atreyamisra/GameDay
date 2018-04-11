package com.example.pratyushsingh.scoreapp;

/**
 * Created by atreyamisra on 4/5/18.
 */

public class PlayerAPI {
    private String id;
    private String firstName;
    private String lastName;
    private String teamId;
    private String position;
    private String bio;
    private float ppg;


    public PlayerAPI(String id, String firstName, String lastName, String teamId, String position, String bio, float ppg){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.teamId=teamId;
        this.position=position;
        this.bio=bio;
        this.ppg=ppg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public float getPpg() {
        return ppg;
    }

    public void setPpg(float ppg) {
        this.ppg = ppg;
    }
}

package com.example.pratyushsingh.scoreapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.*;
import java.lang.*;
import org.json.*;

/**
 * Created by pratyushsingh on 3/22/18.
 */


@Entity(foreignKeys = @ForeignKey(entity = Team.class, parentColumns = "id", childColumns = "teamId"))
public class Player {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "firstName")
    private String firstName;

    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "teamId")
    private int teamId;

    @ColumnInfo(name = "id")
    private int playerId;

    @ColumnInfo(name = "pos")
    private String pos;

    @ColumnInfo(name = "bio")
    private String bio;


    public int getPlayerId() {
        return playerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPos() {
        return pos;
    }

    public String getBio() {
        return bio;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    private void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    private void setTeamId(int teamId) {
       this.teamId = teamId;
    }

    public Player(String firstName, String lastName, String pos, String bio, int playerId, int teamId) {
        setFirstName(firstName);
        setLastName(lastName);
        setPos(pos);
        setPlayerId(playerId);
        setBio(bio);
        setTeamId(teamId);
    }




}

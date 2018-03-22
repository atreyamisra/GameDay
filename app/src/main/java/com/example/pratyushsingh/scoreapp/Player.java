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


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    private void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Player(String firstName, String lastName, String pos, int playerId) {
        setFirstName(firstName);
        setLastName(lastName);
        setPos(pos);
        setPlayerId(playerId);

        //do we need to pre-populate the teamId or when we instantiate teamId?
    }




}

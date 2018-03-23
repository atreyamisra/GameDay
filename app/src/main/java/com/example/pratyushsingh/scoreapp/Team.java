package com.example.pratyushsingh.scoreapp;

import android.arch.persistence.room.Entity;
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

@Entity
public class Team {
    public Team(String fullName, int teamId, String nickname, String confName, String divName, String city, String tricode, String urlName){
        this.fullName=fullName;
        this.teamId=teamId;
        this.nickname=nickname;
        this.confName=confName;
        this.divName=divName;
        this.city=city;
        this.tricode=tricode;
        this.urlName=urlName;
    }

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "fullName")
    private String fullName;

    @ColumnInfo(name = "teamId")
    private int teamId;

    @ColumnInfo(name = "nickname")
    private String nickname;

    @ColumnInfo(name = "confName")
    private String confName;

    @ColumnInfo(name = "divName")
    private String divName;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "tricode")
    private String tricode;

    @ColumnInfo(name = "urlName")
    private String urlName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTricode() {
        return tricode;
    }

    public void setTricode(String tricode) {
        this.tricode = tricode;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }





}

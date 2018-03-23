package com.example.pratyushsingh.scoreapp;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Created by pratyushsingh on 3/22/18.
 */


@Dao
public interface PlayerDAO {
    @Insert
    void insertAll(Player... players);

}

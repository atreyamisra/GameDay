package com.gameday.core.backend.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gameday.core.backend.core.AsyncResponse.AsyncResponse;

import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity implements AsyncResponse {
    private ArrayList<Player> players = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        //RetrieveData retrieveData = new RetrieveData(this);
        //retrieveData.execute();

         Player player = new Player();
         player.delegate = this;
         player.getTopTwenty(this);


    }

    @Override
    public void processFinish(ArrayList<?> response) {
        for(Object r: response) {
            players.add((Player) r);
            Player j = (Player) r;
            Log.e("RESULTS: ", j.getName());
        }
    }
}

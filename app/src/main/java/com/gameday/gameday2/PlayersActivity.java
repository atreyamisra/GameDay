package com.gameday.gameday2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity implements AsyncResponse {
    ListView lvPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RetrieveData retrieveData = new RetrieveData(this);
//        retrieveData.execute();
        setContentView(R.layout.activity_players);
        lvPlayers = (ListView) findViewById(R.id.lv_players);
        Player player = new Player();
        player.delegate = this;
        player.getTopTwenty(getApplicationContext());

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        getSupportActionBar().setLogo(R.drawable.toolbar_image);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem() != null ) {
                    Log.d("PLAYER CLICKED: ", adapterView.getSelectedItem().toString());
                }
            }
        });
    }

    @Override
    public void processFinish(ArrayList<?> response) {
        Player[] players = new Player[20];
        for(int i = 0; i < response.size(); i++) {
            Player player = (Player) response.get(i);
            players[i] = player;

        }
        lvPlayers.setAdapter(new PlayersListViewAdapter(this, players));

    }
}

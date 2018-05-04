package com.gameday.gameday2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Intent myIntent = getIntent(); // gets the previously created intent
        Game game = (Game) myIntent.getSerializableExtra("game"); // will return "FirstKeyValue"
        Log.d("GOT THE EXTRA", game.getGameId());
    }
}

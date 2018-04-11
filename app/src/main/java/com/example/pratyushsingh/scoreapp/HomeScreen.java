package com.example.pratyushsingh.scoreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    Button nextButton;
    TextView playersList;
    RetrieveData database;
    boolean mode = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        playersList = findViewById(R.id.tvPlayers);
        nextButton = findViewById(R.id.btnNext);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute();
            }
        });

        database = RetrieveData.getInstance();
        database.execute();

        execute();
    }

    public void execute() {
        ArrayList<PlayerAPI> players = database.getPlayers();

        if (mode) {

            String top10="";
            for(int i = 0; i < 10; i++){
                PlayerAPI player = players.get(i);
                top10 +="\n"+player.getFirstName()+" "+player.getLastName();
            }
            playersList.setText(top10);
            nextButton.setText("next");
        } else {

            String next10="";
            for(int i = 10; i < 20; i++){
                PlayerAPI player = players.get(i);
                next10 +="\n"+player.getFirstName()+" "+player.getLastName();
            }
            playersList.setText(next10);
            nextButton.setText("back");
        }
        mode = !mode;
    }


}

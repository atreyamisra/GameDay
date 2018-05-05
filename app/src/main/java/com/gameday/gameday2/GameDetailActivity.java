package com.gameday.gameday2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameDetailActivity extends AppCompatActivity implements GameDetailAsyncResponse{

    ListView lvHeadToHead;
    ListView lvTeam1PastGames;
    ListView lvTeam2PastGames;
    TextView tvTeam1Name;
    TextView tvTeam2Name;
    ImageView ivTeam1;
    ImageView ivTeam2;
    TextView tvTeam1Score;
    TextView tvTeam2Score;
    TextView tvTime;
    TextView tvQuarter;

    ArrayList<Player> team1Players;
    ArrayList<Player> team2PLayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Intent myIntent = getIntent(); // gets the previously created intent
        Game game = (Game) myIntent.getSerializableExtra("game"); // will return "FirstKeyValue"
        Log.d("GOT THE EXTRA", game.getGameId());

        GameDetail.delegate = this;
        GameDetail.getGameDetails(game.getGameId(), "20180503", this);

        lvHeadToHead = (ListView) findViewById(R.id.lv_head_to_head);
        // lvHeadToHead.setAdapter(new HeadToHeadListViewAdapter(this, new String[3]));

        lvTeam1PastGames = (ListView) findViewById(R.id.lv_home_games);
        lvTeam1PastGames.setAdapter(new PastGameListViewAdapter(this, new Game[3]));

        lvTeam2PastGames = (ListView) findViewById(R.id.lv_visitor_games);
        lvTeam2PastGames.setAdapter(new PastGameListViewAdapter(this, new Game[3]));

        tvTeam1Name = findViewById(R.id.tv_name_team_1);
        tvTeam1Score = findViewById(R.id.tv_score_team_1);
        ivTeam1 = findViewById(R.id.iv_team_1);

        tvTeam2Name = findViewById(R.id.tv_name_team_2);
        tvTeam2Score = findViewById(R.id.tv_score_team_2);
        ivTeam2 = findViewById(R.id.iv_team_2);

        tvTime = findViewById(R.id.tv_time);
        tvQuarter = findViewById(R.id.tv_quarter);

        tvTeam1Name.setText(game.getHomeTeam());
        ivTeam1.setImageBitmap(game.gethTeamLogo().bitmap);
        tvTeam1Score.setText(game.getHscore());

        tvTeam2Name.setText(game.getVisitingTeam());
        ivTeam2.setImageBitmap(game.getvTeamLogo().bitmap);
        tvTeam2Score.setText(game.getvScore());
        tvTime.setText(game.getClock());


    }

    @Override
    public void processFinish(GameDetailResults results) {
        team1Players = results.gethTeamPlayers();
        team2PLayers = results.getvTeamPlayers();

        lvHeadToHead.setAdapter(new HeadToHeadListViewAdapter(this, team1Players, team2PLayers));
    }
}

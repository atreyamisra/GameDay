package com.gameday.gameday2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentGamesFragment extends Fragment implements AsyncResponse {
    // Store instance variables
    private String title;
    private int page;
    RecyclerView rvGames;
    TextView tvTeam1Name;
    TextView tvTeam2Name;
    ImageView ivTeam1;
    ImageView ivTeam2;
    TextView tvTeam1Score;
    TextView tvTeam2Score;
    TextView tvTime;
    TextView tvQuarter;

    // newInstance constructor for creating fragment with arguments
    public static CurrentGamesFragment newInstance(int page, String title) {
        CurrentGamesFragment fragmentFirst = new CurrentGamesFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        Game.delegate = this;
        Game.getCurrentGame();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currentgames, container, false);
        tvTeam1Name = view.findViewById(R.id.tv_name_team_1);
        tvTeam1Score = view.findViewById(R.id.tv_score_team_1);
        ivTeam1 = view.findViewById(R.id.iv_team_1);

        tvTeam2Name = view.findViewById(R.id.tv_name_team_2);
        tvTeam2Score = view.findViewById(R.id.tv_score_team_2);
        ivTeam2 = view.findViewById(R.id.iv_team_2);

        tvTime = view.findViewById(R.id.tv_time);
        tvQuarter = view.findViewById(R.id.tv_quarter);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            names.add("Warriors");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvGames = view.findViewById(R.id.rv_games);
        rvGames.setLayoutManager(layoutManager);
    }

    @Override
    public void processFinish(ArrayList<?> response) {
        ArrayList<Game> games = new ArrayList<>();
        for(int i = 1; i < response.size(); i++) {
            Game game = (Game) response.get(i);
            games.add(game);
            Log.d("GAME: ", game.getGameId());
        }
        Game trendingGame = (Game) response.get(0);
        tvTeam1Name.setText(trendingGame.getHomeTeam());
        tvTeam1Score.setText(trendingGame.getHscore());
        ivTeam1.setImageBitmap(trendingGame.gethTeamLogo());

        tvTeam2Name.setText(trendingGame.getVisitingTeam());
        tvTeam2Score.setText(trendingGame.getvScore());
        ivTeam2.setImageBitmap(trendingGame.getvTeamLogo());

        tvTime.setText(trendingGame.getClock());
        if (trendingGame.getIsActive()) {
            tvQuarter.setText(trendingGame.getPeriod());
        }
        //pbLoading.setVisibility(View.GONE);
        rvGames.setAdapter(new GameRecyclerViewAdapter(getContext(), games.toArray(new Game[games.size()])));
    }
}

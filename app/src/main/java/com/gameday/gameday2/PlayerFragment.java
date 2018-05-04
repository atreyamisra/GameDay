package com.gameday.gameday2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class PlayerFragment extends Fragment implements AsyncResponse {
    // Store instance variables
    ListView lvPlayers;
    ProgressBar pbLoading;

    // newInstance constructor for creating fragment with arguments
    public static PlayerFragment newInstance(int page, String title) {
        PlayerFragment fragmentFirst = new PlayerFragment();
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
        RetrieveData retrieveData = new RetrieveData(getContext());
        retrieveData.execute();
        Player player = new Player();
        player.delegate = this;
        player.getTopTwenty(getContext());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        lvPlayers = (ListView) view.findViewById(R.id.lv_players);
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem() != null ) {
                    Log.d("PLAYER CLICKED: ", adapterView.getSelectedItem().toString());
                }
            }
        });

        // on some click or some loading we need to wait for...
        pbLoading = (ProgressBar) view.findViewById(R.id.progress_bar);
        pbLoading.setVisibility(ProgressBar.VISIBLE);

        return view;

    }

    @Override
    public void processFinish(ArrayList<?> response) {
        Player[] players = new Player[20];
        for(int i = 0; i < response.size(); i++) {
            Player player = (Player) response.get(i);
            players[i] = player;
            Log.d("PLAYERS: ", player.getName());
        }
        pbLoading.setVisibility(View.GONE);
        lvPlayers.setAdapter(new PlayersListViewAdapter(getContext(), players));

    }
}
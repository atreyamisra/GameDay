package com.gameday.gameday2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        Intent myIntent = getIntent(); // gets the previously created intent
        Player player = (Player) myIntent.getSerializableExtra("player"); // will return "FirstKeyValue"
        Log.d("GOT THE EXTRA", player.getName());

        // st up view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView profilePhoto = (ImageView) findViewById(R.id.player_photo);
        TextView playerName = (TextView) findViewById(R.id.player_name);
        TextView team = (TextView) findViewById(R.id.team);
        TextView numberPosition = (TextView) findViewById(R.id.number_position);
        TextView about = (TextView) findViewById(R.id.about);
        TextView bio = (TextView) findViewById(R.id.bio);

        profilePhoto.setImageBitmap(player.getProfilePhoto().bitmap);
        playerName.setText(player.getName().toUpperCase());
        playerName.setTextColor(getResources().getColor(R.color.textLight));
        playerName.setTextSize(24);
        team.setText(player.getTeam());
        team.setTextColor(getResources().getColor(R.color.textLight));
        team.setTextSize(18);
        // number
        numberPosition.setText(player.getPosition());
        numberPosition.setTextColor(getResources().getColor(R.color.textLight));
        numberPosition.setTextSize(18);
        about.setText("About " + player.getName() + "\n");
        about.setTextSize(22);
        bio.setText(player.getBiography());
        bio.setTextSize(18);

    }

}

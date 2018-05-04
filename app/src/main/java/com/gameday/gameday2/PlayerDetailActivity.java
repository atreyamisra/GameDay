package com.gameday.gameday2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Player player = new Player();
        CircleImageView profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        TextView playerName = (TextView) findViewById(R.id.player_name);
        TextView team = (TextView) findViewById(R.id.team);
        TextView number = (TextView) findViewById(R.id.number);
        TextView position = (TextView) findViewById(R.id.position);
        TextView bio = (TextView) findViewById(R.id.bio);

        profilePhoto.setImageBitmap(player.getProfilePhoto());
        playerName.setText(player.getName());
        team.setText(player.getTeam());
        // number
        position.setText(player.getPosition());
        bio.setText(player.getBiography());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

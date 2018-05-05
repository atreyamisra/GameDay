package com.gameday.gameday2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HeadToHeadListViewAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Player> team1;
    ArrayList<Player> team2;

    public HeadToHeadListViewAdapter(Context context, ArrayList<Player> team1, ArrayList<Player> team2) {
        this.context = context;
        this.team1 = team1;
        this.team2 = team2;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return team1.size();
    }

    @Override
    public Object getItem(int position) {
        return team1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.cell_headtohead, null);
        TextView tvPlayer1Score = (TextView) vi.findViewById(R.id.tv_player_1);
        TextView tvPlayer2Score = (TextView) vi.findViewById(R.id.tv_player_2);
        ImageView ivPlayer1 = (ImageView) vi.findViewById(R.id.iv_player_1);
        ImageView ivPlayer2 = (ImageView) vi.findViewById(R.id.iv_player_2);
        TextView tvPlayer1 = (TextView) vi.findViewById(R.id.tv_player_1_name);
        TextView tvPlayer2 = (TextView) vi.findViewById(R.id.tv_player_2_name);

        tvPlayer1Score.setText(team1.get(position).getPointsScoredInGame());
        tvPlayer2Score.setText(team1.get(position).getPointsScoredInGame());
        ivPlayer1.setImageBitmap(team2.get(position).getProfilePhoto().bitmap);
        ivPlayer2.setImageBitmap(team2.get(position).getProfilePhoto().bitmap);
        tvPlayer1.setText(team1.get(position).getName());
        tvPlayer2.setText(team2.get(position).getName());
        return vi;
    }
}

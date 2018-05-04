package com.gameday.gameday2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PastGameListViewAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    Game[] games;

    public PastGameListViewAdapter(Context context, Game[] data) {
        this.context = context;
        this.games = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return games.length;
    }

    @Override
    public Object getItem(int position) {
        return games[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.cell_pastgames, null);
//        TextView tvPlayer1Score = (TextView) vi.findViewById(R.id.tv_player_1);
//        TextView tvPlayer2Score = (TextView) vi.findViewById(R.id.tv_player_2);
//        ImageView ivPlayer1 = (ImageView) vi.findViewById(R.id.iv_player_1);
//        ImageView ivPlayer2 = (ImageView) vi.findViewById(R.id.iv_player_2);

//        tvPlayer1Score.setText();
//        tvPlayer2Score.setText();
//        ivPlayer1.setImageBitmap();
//        ivPlayer2.setImageBitmap();

            TextView tvTeam1Name = vi.findViewById(R.id.tv_name_team_1);
            TextView tvTeam1Score = vi.findViewById(R.id.tv_score_team_1);
            ImageView ivTeam1 = vi.findViewById(R.id.iv_team_1);

            TextView tvTeam2Name = vi.findViewById(R.id.tv_name_team_2);
            TextView tvTeam2Score = vi.findViewById(R.id.tv_score_team_2);
            ImageView ivTeam2 = vi.findViewById(R.id.iv_team_2);

            TextView tvTime = vi.findViewById(R.id.tv_time);
            TextView tvQuarter = vi.findViewById(R.id.tv_quarter);

//        tvTeam1Name.setText(games[position].getHomeTeam());
//        ivTeam1.setImageBitmap(games[position].gethTeamLogo().bitmap);
//        tvTeam1Score.setText(games[position].getHscore());
//
//        tvTeam2Name.setText(games[position].getVisitingTeam());
//        ivTeam2.setImageBitmap(games[position].getvTeamLogo().bitmap);
//        tvTeam2Score.setText(games[position].getvScore());
//        tvTime.setText(games[position].getClock());
//        tvQuarter.setText(games[position].getPeriod());
        }
        return vi;
    }
}

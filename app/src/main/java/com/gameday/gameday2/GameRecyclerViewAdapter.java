package com.gameday.gameday2;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder>{

    private static final String TAG= "RecyclerViewAdapter";
    private Game[] games;
    Context context;

    public GameRecyclerViewAdapter(Context context, Game[] games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_currentgames, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int
            position) {
        holder.tvTeam1Name.setText(games[position].getHomeTeam());
        holder.ivTeam1.setImageBitmap(games[position].gethTeamLogo().bitmap);
        holder.tvTeam1Score.setText(games[position].getHscore());

        holder.tvTeam2Name.setText(games[position].getVisitingTeam());
        holder.ivTeam2.setImageBitmap(games[position].getvTeamLogo().bitmap);
        holder.tvTeam2Score.setText(games[position].getvScore());
        holder.tvTime.setText(games[position].getClock());

        if (games[position].getIsActive()) {
            holder.tvQuarter.setText(games[position].getPeriod());
        }

        holder.cvGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED", "I clicked a view holder");
                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra("game", games[position]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return games.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeam1Name;
        TextView tvTeam2Name;
        ImageView ivTeam1;
        ImageView ivTeam2;
        TextView tvTeam1Score;
        TextView tvTeam2Score;
        TextView tvTime;
        TextView tvQuarter;
        CardView cvGame;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTeam1Name = itemView.findViewById(R.id.tv_name_team_1);
            tvTeam1Score = itemView.findViewById(R.id.tv_score_team_1);
            ivTeam1 = itemView.findViewById(R.id.iv_team_1);

            tvTeam2Name = itemView.findViewById(R.id.tv_name_team_2);
            tvTeam2Score = itemView.findViewById(R.id.tv_score_team_2);
            ivTeam2 = itemView.findViewById(R.id.iv_team_2);

            tvTime = itemView.findViewById(R.id.tv_time);
            tvQuarter = itemView.findViewById(R.id.tv_quarter);

            cvGame = itemView.findViewById(R.id.cvGame);
        }
    }
}

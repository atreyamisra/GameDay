package com.gameday.gameday2;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder>{

    private static final String TAG= "RecyclerViewAdapter";
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageURLs = new ArrayList<>();
    Context context;

    public GameRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        this.context = context;
        this.names = names;
        this.imageURLs = imageUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_currentgames, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTeam1Name.setText(names.get(position));
        holder.tvTeam2Name.setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
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
        }
    }
}

package com.gameday.gameday2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aashimagarg on 4/26/18.
 */

class PlayersListViewAdapter extends BaseAdapter {

    Context context;
    Player[] data;
    private static LayoutInflater inflater = null;

    public PlayersListViewAdapter(Context context, Player[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.cell_players, null);
        TextView tvName = (TextView) vi.findViewById(R.id.tv_name);
        TextView tvTeam = (TextView) vi.findViewById(R.id.tv_team);
        CircleImageView ivProfilePhoto = (CircleImageView) vi.findViewById(R.id.iv_profile_photo);
        tvName.setText(data[position].getName());
        tvTeam.setText(data[position].getTeam());
        ivProfilePhoto.setImageBitmap(data[position].getProfilePhoto());
        return vi;
    }
}
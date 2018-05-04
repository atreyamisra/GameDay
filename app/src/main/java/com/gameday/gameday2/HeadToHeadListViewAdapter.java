package com.gameday.gameday2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeadToHeadListViewAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    String[] data;

    public HeadToHeadListViewAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
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

//        tvPlayer1Score.setText();
//        tvPlayer2Score.setText();
//        ivPlayer1.setImageBitmap();
//        ivPlayer2.setImageBitmap();

        return vi;
    }
}

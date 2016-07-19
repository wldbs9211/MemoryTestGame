package com.jiyun.jiyun.memorytest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewRankingDataAdapter extends BaseAdapter {
    ArrayList<ListViewRankingData> datas;
    LayoutInflater inflater;

    public ListViewRankingDataAdapter(LayoutInflater inflater, ArrayList<ListViewRankingData> datas) {
        // TODO Auto-generated constructor stub
        this.datas= datas;
        this.inflater= inflater;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        if( convertView==null){
            convertView= inflater.inflate(R.layout.ranking_listview_item, null);
        }

        TextView text_Number= (TextView)convertView.findViewById(R.id.RankingNumber);
        text_Number.setText( datas.get(position).getNumber() );
        TextView text_ID= (TextView)convertView.findViewById(R.id.RankingID);
        text_ID.setText( datas.get(position).getID() );
        TextView text_Score= (TextView)convertView.findViewById(R.id.RankingScore);
        text_Score.setText( datas.get(position).getScore() );

        return convertView;
    }

}
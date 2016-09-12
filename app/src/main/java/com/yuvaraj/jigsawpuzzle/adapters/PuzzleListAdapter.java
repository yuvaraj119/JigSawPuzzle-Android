package com.yuvaraj.jigsawpuzzle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yuvaraj.jigsawpuzzle.pojos.Pieces;
import com.yuvaraj.jigsawpuzzle.ui.PuzzleActivity;
import com.yuvaraj.jigsawpuzzle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CIPL0349 on 9/6/2016.
 */
public class PuzzleListAdapter extends BaseAdapter {

    Context mContext;
    List<Pieces> piecesModelList = new ArrayList<Pieces>();

    public PuzzleListAdapter(Context mContext, List<Pieces> piecesModelList) {
        this.mContext = mContext;
        this.piecesModelList = piecesModelList;
    }

    @Override
    public int getCount() {
        return piecesModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return piecesModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater Inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView imageView;

        if (convertView == null)
            convertView = Inflater.inflate(R.layout.puzzle_item, null);


        imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageResource(piecesModelList.get(position).getOriginalResource());
        imageView.setTag("" + piecesModelList.get(position).getpX() + "," + piecesModelList.get(position).getpY());

        imageView.setOnLongClickListener(new PuzzleActivity.MyClickListener());

        return convertView;
    }
}

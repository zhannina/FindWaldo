package com.example.zhanna.findwaldo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by zhanna on 08/11/15.
 */
public class GridViewCustomAdapter extends ArrayAdapter {
    Context context;
    ArrayList<MainActivity.CellContent> icons;
    HashMap<Integer, MainActivity.CellContent> iconsMap = new HashMap<>();
    int positionToFind;

    public GridViewCustomAdapter(Context context, ArrayList<MainActivity.CellContent> nIcons, int posToFind, MainActivity.CellContent cc) {
        super(context, 0);
        this.context = context;
        this.icons = new ArrayList<>(nIcons);
        this.positionToFind = posToFind;
        iconsMap.put(posToFind, cc);
        this.icons.remove(icons.indexOf(cc));

        Collections.shuffle(icons, new Random(System.nanoTime()));

        for (int i = 0; i < 24; i++) {
            if (i != posToFind) {
                iconsMap.put(i, icons.get(0));
                icons.remove(0);
            }
        }

    }

    public int getCount() {
        return 24;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;

        if (cell == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            cell = inflater.inflate(R.layout.grid_cell, parent, false);


            TextView textViewTitle = (TextView) cell.findViewById(R.id.textView);
            ImageView imageViewItem = (ImageView) cell.findViewById(R.id.imageView);

            textViewTitle.setText(iconsMap.get(position).getName());
            imageViewItem.setImageResource(iconsMap.get(position).getDrawableID());

        }

        return cell;

    }
}

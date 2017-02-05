package com.example.jannis.fahrtenapp.DataDisplays.YearDisplay;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jannis.fahrtenapp.Entity.Distance;
import com.example.jannis.fahrtenapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YearViewAdapter extends ArrayAdapter<Distance> {

    private Context context;
    private int layoutResourceId;
    private List<Distance> objects = null;

    public YearViewAdapter(Context context, int layoutResourceId, List<Distance> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        DistanceHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DistanceHolder();
            holder.start = (TextView) row.findViewById(R.id.start);
            holder.stop = (TextView) row.findViewById(R.id.stop);
            holder.distance = (TextView) row.findViewById(R.id.distanz);

            row.setTag(holder);
        } else {
            holder = (DistanceHolder) row.getTag();
        }

        Distance distance = objects.get(position);
        distance.getStart_time();

        String startTime = new SimpleDateFormat("dd.MM/HH:mm", Locale.GERMANY).format(new Date(distance.getStart_time()));
        String stopTime = new SimpleDateFormat("dd.MM/HH:mm", Locale.GERMANY).format(new Date(distance.getStop_time()));
        String s_distance = String.format(Locale.GERMANY, "%.2f", distance.getDistance() / 1000);

        holder.start.setText(startTime);
        holder.stop.setText(stopTime);
        holder.distance.setText(s_distance);

        return row;
    }

    private static class DistanceHolder {
        TextView start;
        TextView stop;
        TextView distance;
    }
}

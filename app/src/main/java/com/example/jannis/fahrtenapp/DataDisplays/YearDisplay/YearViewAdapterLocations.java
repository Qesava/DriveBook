package com.example.jannis.fahrtenapp.DataDisplays.YearDisplay;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
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

public class YearViewAdapterLocations extends ArrayAdapter<Location> {
    private Context context;
    private int layoutResourceId;
    private List<Location> objects = null;

    public YearViewAdapterLocations(Context context, int layoutResourceId, List<Location> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        YearViewAdapterLocations.ZeitHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new YearViewAdapterLocations.ZeitHolder();
            holder.zeitpunkt = (TextView) row.findViewById(R.id.zeitpunkt);
            holder.id = (TextView) row.findViewById(R.id.locId);

            row.setTag(holder);
        } else {
            holder = (YearViewAdapterLocations.ZeitHolder) row.getTag();
        }

        Location location = objects.get(position);

        String startTime = new SimpleDateFormat("dd.MM/HH:mm:ss", Locale.GERMANY).format(new Date(location.getTime()));
        String id = String.valueOf(position + 1);

        holder.zeitpunkt.setText(startTime);
        holder.id.setText(id);
        return row;
    }

    private static class ZeitHolder {
        TextView zeitpunkt;
        TextView id;
    }
}

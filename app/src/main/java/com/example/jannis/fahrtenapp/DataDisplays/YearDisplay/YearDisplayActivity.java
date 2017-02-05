package com.example.jannis.fahrtenapp.DataDisplays.YearDisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.jannis.fahrtenapp.Entity.Distance;
import com.example.jannis.fahrtenapp.R;
import com.example.jannis.fahrtenapp.SaveData.Database.GPSLocationDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YearDisplayActivity extends AppCompatActivity {

    private GPSLocationDataSource dataSource;
    //private MyLocationManager locationManager;
    private List<Distance> distances;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.yearListView);

        dataSource = new GPSLocationDataSource(getApplicationContext());
        dataSource.openRead();
        distances = dataSource.getAllDistances();
        dataSource.close();

        filterThisYearDistances();
        fillList();
    }

    private void filterThisYearDistances() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        for (int i = distances.size() - 1; i >= 0; i--) {
            String datum = new SimpleDateFormat("yyyy", Locale.GERMANY).format(new Date(distances.get(i).getStart_time()));
            if (!datum.equals(String.valueOf(year))) {
                distances.remove(i);
            }
        }
    }

    private void fillList() {
        YearViewAdapter adapter = new YearViewAdapter(this, R.layout.listview_item_year, distances);
        View header = getLayoutInflater().inflate(R.layout.listview_header_year, null);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(null);
    }

}

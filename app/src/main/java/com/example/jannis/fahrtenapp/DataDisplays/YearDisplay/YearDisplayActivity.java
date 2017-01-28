package com.example.jannis.fahrtenapp.DataDisplays.YearDisplay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jannis.fahrtenapp.Entity.MyLocationManager;
import com.example.jannis.fahrtenapp.R;
import com.example.jannis.fahrtenapp.SaveData.Database.GPSLocationDataSource;

public class YearDisplayActivity extends AppCompatActivity {

    private GPSLocationDataSource dataSource;
    MyLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        locationManager = new MyLocationManager();

        dataSource = new GPSLocationDataSource(this);
        dataSource.openRead();
        locationManager.setLocationList(dataSource.getAllGPSLocations());
        dataSource.close();*/
    }

}

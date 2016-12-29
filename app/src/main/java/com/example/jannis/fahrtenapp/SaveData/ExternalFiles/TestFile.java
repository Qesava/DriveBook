package com.example.jannis.fahrtenapp.SaveData.ExternalFiles;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TestFile {
    private Calendar timestamp;
    Activity activity;
    private File dir;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public TestFile(Activity a) {
        this.activity = a;
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /*creates working directory and returns it*/
    public File creatFileDirectory() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DriveBook/");

        if (!file.mkdirs()) {
            //Toast.makeText(activity,String.valueOf(file),Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void saveToFile(double distance) {
        verifyStoragePermissions();
        if (isExternalStorageWritable()) {
            dir = creatFileDirectory();
            File text = new File(dir, "Logs.txt");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(text);
                fileOutputStream.write(String.valueOf(distance).getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "External Storage nicht erreichbar", Toast.LENGTH_SHORT).show();
        }
    }

}

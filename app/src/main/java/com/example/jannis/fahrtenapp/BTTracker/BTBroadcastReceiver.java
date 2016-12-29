package com.example.jannis.fahrtenapp.BTTracker;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.jannis.fahrtenapp.GPSTracker.LocationHandler;


public class BTBroadcastReceiver extends BroadcastReceiver {
    LocationHandler locationHandler = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            //Device is now connected
            //TODO: add bluetoothname
            //if (device.getName().equals("")) {
            //Object obj = new LocationHandler(context).execute();
            //locationHandler= (LocationHandler)obj;
            //locationHandler.startListening();
            Log.i("Info BTReceiver", "Bluetooth connected");
            //}
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            //Device is about to disconnect
            if (locationHandler != null) {
                locationHandler.stopListening();
                Log.i("Info BTReceiver", "Bluetooth about to disconnect");
            }
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            //Device has disconnected
        }
    }
}

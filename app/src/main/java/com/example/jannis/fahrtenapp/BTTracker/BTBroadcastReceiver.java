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
    Intent intent1;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            //Device is now connected
            //TODO: add bluetoothname
            if (device.getName().equals("BM-E9")) {
                intent1 = new Intent(context, LocationHandler.class);
                context.startService(intent1);
                Log.i("Info BTReceiver", "Bluetooth connected");
            }
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            //Device is about to disconnect
                Log.i("Info BTReceiver", "Bluetooth about to disconnect");
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            context.stopService(intent1);
            //Device has disconnected
        }
    }
}

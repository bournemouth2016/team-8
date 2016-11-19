package com.wellsetaboutye.glesga;

import android.content.Context;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String BEACON_NAME = "MiniBeacon_05440";
    private static final int MAXISH = 6; // some number bigger than the distance will ever be

    protected static final String TAG = "MainActivity";
    private BeaconManager kevinBeaconManager;
    private Beacon tracked = null;
    private Vibrator vibrator;
    private TextView distanceTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "fine runner beans");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kevinBeaconManager = BeaconManager.getInstanceForApplication(this);
        kevinBeaconManager.bind(this);
        kevinBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kevinBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "working a bit");

        kevinBeaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                long time = System.nanoTime();
                Log.i(TAG, "working seriously " + collection.size());
                for (Beacon b : collection) {
                    //Log.i(TAG, time + " " + b.getBluetoothName() + " " + b.getDistance() + " " + b.getTxPower());
                    if (b.getBluetoothName().equals(BEACON_NAME)) {
                        tracked = b;
                    }
                }
                if (tracked != null) {
                    Log.i(TAG, "tracked @ " + tracked.getDistance());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.distanceTextView)).setText(String.format("%.2f", tracked.getDistance()));
                        }
                    });
                }
            }
        });

        try {
            kevinBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.i(TAG, "something has gone horribly wrong here");
        }
    }
}

package com.wellsetaboutye.glesga;

import android.os.Bundle;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BadNumbersActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String BEACON_NAME = "MiniBeacon_05440";

    protected static final String TAG = "MainActivity";
    private BeaconManager kevinBeaconManager;
    private Beacon tracked = null;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kevinBeaconManager = BeaconManager.getInstanceForApplication(this);
        kevinBeaconManager.bind(this);
        kevinBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                ; // have a party
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kevinBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        kevinBeaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                for (Beacon b : collection) {
                    if (b.getBluetoothName().equals(BEACON_NAME)) {
                        tracked = b;
                    }
                }
                if (tracked != null) {
                    Log.i(TAG, "tracked @ " + tracked.getDistance());
                    final String niceDistanceString = String.format("%.2f", tracked.getDistance());
                    tts.speak(niceDistanceString, TextToSpeech.QUEUE_FLUSH, null);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.distanceTextView)).setText(niceDistanceString);
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

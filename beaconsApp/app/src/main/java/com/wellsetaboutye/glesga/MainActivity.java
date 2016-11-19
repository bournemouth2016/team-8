package com.wellsetaboutye.glesga;

import android.content.Context;
import android.os.RemoteException;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
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
    private TextToSpeech tts;
    private boolean seenBeacon = false;

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
        Log.i(TAG, "working a bit");

        kevinBeaconManager.addRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i(TAG, seenBeacon ? "seen!" : "notSeen...");
                if (!seenBeacon) {
                    for (Beacon b : collection) {
                        if (b.getBluetoothName().equals(BEACON_NAME)) {
                            seenBeacon = true;
                            speakTheTruth();
                            break;
                        }
                    }
                }
            }
        });

        try {
            kevinBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.i(TAG, "something has gone horribly wrong here");
        }
    }

    private void speakTheTruth() {
        tts.speak("In auditorium. Stage near right. Chairs directly ahead following semi-circular shape of room. Fire exit far right past stage.", TextToSpeech.QUEUE_FLUSH, null);
    }
}

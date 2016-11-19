package com.wellsetaboutye.glesga;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by ecorson on 19/11/16.
 */

public class FroggingButtonsActivity extends AppCompatActivity {

    private static final String TAG = "TTSActivity";

    private Button clickButton;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "fine runner beans");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextToSpeech.OnInitListener listener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                ; // have a party
            }
        };

        tts = new TextToSpeech(this, listener);

        //tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);

        clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "FROGGING BUTTONS");
                tts.speak("FROGGING BUTTONS", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}

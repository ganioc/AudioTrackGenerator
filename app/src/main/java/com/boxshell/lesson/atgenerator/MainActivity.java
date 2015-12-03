package com.boxshell.lesson.atgenerator;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boxshell.lesson.atgenerator.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "ATGenerator";
    private static Button mSendBtn;
    private static int mSampleRate;
    private static int mCount;
    private static int mSoundHz;
    private static byte[] data;
    private AudioTrack mAudioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendBtn = (Button) findViewById(R.id.button);

        mSendBtn.setOnClickListener(this);

        init_audio();

    }

    private void init_audio() {
        L("samplerate notification(Hz) :" + printSampleRate(AudioManager.STREAM_NOTIFICATION));
        L("samplerate alarm :" + printSampleRate(AudioManager.STREAM_ALARM));
        L("samplerate  dtmf :" + printSampleRate(AudioManager.STREAM_DTMF));
        L("samplerate  music:" + printSampleRate(AudioManager.STREAM_MUSIC));
        L("samplerate  ring:" + printSampleRate(AudioManager.STREAM_RING));
        L("samplerate  system:" + printSampleRate(AudioManager.STREAM_SYSTEM));
        L("sampeerate voice call: " + printSampleRate(AudioManager.STREAM_VOICE_CALL));

        // on nexus 4, it's 48 kHz
        mSampleRate = printSampleRate(AudioManager.STREAM_MUSIC);
        mSoundHz = 1000;



    }


    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int sampleRate = 48000;  // 44100 Hz

        int count = (int)( sampleRate * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (sampleRate / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);

        return track;
    }

    private void L(String str) {
        Log.d(TAG, str);
    }

    private int printSampleRate(int type) {

        return AudioTrack.getNativeOutputSampleRate(type);
    }

    @Override
    public void onClick(View v) {
        //
        Log.d(TAG, "Clicked:" + v.getId());
        Log.d(TAG, "btn is:" + R.id.button);


        switch (v.getId()) {
            case R.id.button:
                Log.d(TAG, "capture it");
                //Toast.makeText(MainActivity.this, "Send", Toast.LENGTH_LONG).show();
                //mAudioTrack.play();

                AudioTrack mATrack = generateTone(3000, 2000);
                mATrack.play();

                break;
            default:
                break;
        }

    }
}

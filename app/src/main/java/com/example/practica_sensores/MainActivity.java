package com.example.practica_sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor proximitySensor;
    MediaPlayer mediaPlayer;
    TextView tvSongStatus,cancion;
    SensorEventListener sensorEventListener;
    float lastX, lastY, lastZ;
    boolean firstTime = true;
    float x,y,z,deltaX,deltaY,deltaZ;
    ImageView im1;

    Sensor accelerometerSensor;
    float THRESHOLD = 10f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        im1 = findViewById(R.id.im1);
        mediaPlayer = MediaPlayer.create(this, R.raw.avicii);
        tvSongStatus = findViewById(R.id.tv_song_status);
        cancion = findViewById(R.id.cancion);
        cancion.setText("Avicci-the nights");

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                tvSongStatus.setText("Song Status: Completed");
            }
        });
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float distance = event.values[0];
                    if (distance == 0) {
                        pauseSong();
                    } else {
                        playSong();
                    }
                }else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];
                }
                if (firstTime){
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    firstTime =false;
                }
                else{
                    deltaX = Math.abs(lastX - x);
                    deltaY = Math.abs(lastY - y);
                    deltaZ = Math.abs(lastZ - z);
                }
                if(deltaX >THRESHOLD){
                    if(x > 0)
                    {
                        changeSong(true);
                    }
                    else
                    {
                        changeSong(false);
                    }
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    private void playSong() {
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                tvSongStatus.setText("Status: Play");

            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error playing audio file: " + e.getMessage());
        }
    }

    private void pauseSong() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                tvSongStatus.setText("Status: Pause");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error pausing audio file: " + e.getMessage());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
    private void changeSong(boolean next) {
        int currentSongIndex = mediaPlayer.getCurrentPosition();
        if (next) {
            currentSongIndex += 1;
            if (currentSongIndex > 2) {
                currentSongIndex = 2;
            }
        } else {
            currentSongIndex -= 1;
            if (currentSongIndex < 1) {
                currentSongIndex = 0;
            }
        }

        switch (currentSongIndex) {
            case 0:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    im1.setImageResource(R.drawable.hayya);
                    mediaPlayer = MediaPlayer.create(this, R.raw.hayya);
                    cancion.setText("hayya- cancion oficial mundial 2022");
                    tvSongStatus.setText("Status: Play");
                    mediaPlayer.start();

                }


                break;
            case 1:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    im1.setImageResource(R.drawable.avici1);
                    mediaPlayer = MediaPlayer.create(this, R.raw.avicii);
                    cancion.setText("Avicci-the nights");
                    tvSongStatus.setText("Status: Play");
                    mediaPlayer.start();
                }

                break;
            case 2:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    im1.setImageResource(R.drawable.quevedo);
                    mediaPlayer = MediaPlayer.create(this, R.raw.quevedo);
                    cancion.setText("QUEVEDO || BZRP Music Sessions #52");
                    tvSongStatus.setText("Status: Play");
                    mediaPlayer.start();

                }
                break;
        }

    }
}


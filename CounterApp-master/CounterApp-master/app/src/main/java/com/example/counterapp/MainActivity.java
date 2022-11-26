package com.example.counterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    int counter = 0;
    Button btn_add, btn_sub, btn_settings;
    TextView tv_counter;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    SqliteHelper db;
    String isActive;
    int topLimit;
    int botLimit;
    int sound;
    int vibrate;
    Vibrator vibrator;

    // Tüm nesnelerimizi ve sınıflarımızı burada tanımladık


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // ses açma ve kısma tuşlarına basıldığında çalışan yer

        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            // Volume up tuşunun aksiyonu eğer limit aktifse ve verdiğimiz üst limitin 5 altındaysa çalışıcak basıldığında
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(isActive.equals("active")&&counter<topLimit-4){
                        counter= counter+5;
                        tv_counter.setText(String.valueOf(counter));
                    }else{
                        Toast.makeText(this, "Counter limitine ulaştınız", Toast.LENGTH_SHORT).show();
                        try {
                            // sesi çalma fonksiyonu
                            playSound(MainActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // titreşim fonksiyonu
                        if(vibrate==1){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(1000);
                            }
                        }
                    }

                }
                return true;
            // Volume down tuşunun aksiyonu eğer limit aktifse ve verdiğimiz alt limitin 5 üstündeyse çalışıcak basıldığında
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(isActive.equals("active")&&counter>botLimit+4){
                        counter= counter-5;
                        tv_counter.setText(String.valueOf(counter));
                    }else{
                        Toast.makeText(this, "Counter limitine ulaştınız", Toast.LENGTH_SHORT).show();
                        try {
                            // ses çalma fonksiyonu
                            playSound(MainActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // titreşim fonksiyonu
                        if(vibrate==1){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(1000);
                            }
                        }
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //nesneleri ve widgetları bağlıyoruz
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        btn_add = findViewById(R.id.btn_add);
        btn_sub = findViewById(R.id.btn_sub);
        btn_settings = findViewById(R.id.btn_settings);
        tv_counter = findViewById(R.id.tv_counter);
        db = new SqliteHelper(getApplicationContext());
        StoreDataList();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                counter=0;
                tv_counter.setText(String.valueOf(counter));
            }
        });
        btn_add.setOnClickListener(v -> {
            // arttırma butonuna basıldığında çalışan yer

            if (isActive.equals("active") && counter<topLimit){
                counter++;
                tv_counter.setText(String.valueOf(counter));

            }else{
                Toast.makeText(MainActivity.this, "Sınıra ulaşıldı", Toast.LENGTH_SHORT).show();
                try {
                    playSound(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(vibrate==1){
                    // eğer titreşim aktifse çalışacak
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.d("Titreşim", "Titreşim Gönderildi ");
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(1000);
                        Log.d("Titreşim", "Titreşim Gönderildi ");
                    }
                }
            }
        });

        btn_sub.setOnClickListener(v -> {
            // azaltma butonuna basıldığında çalışan yer
            if (isActive.equals("active") && counter>botLimit){
                counter--;
                tv_counter.setText(String.valueOf(counter));
            }else{
                Toast.makeText(MainActivity.this, "Sınıra ulaşıldı", Toast.LENGTH_SHORT).show();
                try {
                    playSound(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(vibrate==1){
                    // eğer titreşim aktifse çalışacak
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.d("Titreşim", "Titreşim Gönderildi ");
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(1000);
                        Log.d("Titreşim", "Titreşim Gönderildi ");
                    }
                }

            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ayarlar butonuna basıldığında çalışan yer
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }
    public void playSound(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {
        // ses çalma fonksiyonu
        if(sound==1){
            //Eğer ses aktifse çalışıcak
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, soundUri);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                // Uncomment the following line if you aim to play it repeatedly
                // mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        }

    }
    void StoreDataList(){//veritabanından veri çekme
        Cursor cursor=db.GetCounter();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Hiç eski ayar yok", Toast.LENGTH_SHORT).show();// bu toast mesajına hiç bir zaman erişilemiyecek çünkü verilerimizi uygulama başlar başlamaz giriyoruz.
        }else{
            while(cursor.moveToNext()){
                isActive=cursor.getString(1);// ikinci sütundaki veriyi aldık
                topLimit=cursor.getInt(2); // üçüncü sütundaki veriyi aldık
                botLimit=cursor.getInt(3); // 4. sütundaki veriyi aldık
                sound=cursor.getInt(4); // 5. sütundaki veriyi aldık
                vibrate=cursor.getInt(5); // 6. sütundaki veriyi aldık
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        // geri butonuna basıp main activity e döndüğümüzde verilerimizi tekrar çekiyoruz.
        StoreDataList();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }



}
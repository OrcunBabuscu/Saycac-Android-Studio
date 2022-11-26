package com.example.counterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {
    Switch switch_btn,isVibrate_btn,isSound_btn;
    SqliteHelper db;
    String isActive;
    int topLimit;
    int botLimit;
    int sound;
    int vibrate;
    TextView tv_topLimit,tv_botLimit;
    Button btn_save;
    //Nesneleri tanımlıyoruz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //nesneleri ve widgetları bağlıyoruz
        db=new SqliteHelper(getApplicationContext());
        btn_save=findViewById(R.id.btn_save);
        tv_topLimit=findViewById(R.id.tv_counterTop);
        tv_botLimit=findViewById(R.id.tv_counterBottom);

        isVibrate_btn=findViewById(R.id.isVibrate_btn);
        isSound_btn=findViewById(R.id.isSound_btn);
        switch_btn = findViewById(R.id.switch_btn);
        switch_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (switch_btn.isChecked()) //switch onaylı mı?
                {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaylı ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaysız", Toast.LENGTH_SHORT).show();
                }
            }
        });
        isSound_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (isSound_btn.isChecked()) //switch onaylı mı?
                {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaylı ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaysız", Toast.LENGTH_SHORT).show();
                }
            }
        });
        isVibrate_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (isVibrate_btn.isChecked()) //switch onaylı mı?
                {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaylı ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Switch, tıklama dinleyicisi kullanılarak onaysız", Toast.LENGTH_SHORT).show();
                }
            }
        });
        StoreDataList();
        fillWidgets();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCounter();
            }
        });
    }
    void fillWidgets(){

        //Veritabanından gelen verielrle textleri ve switchleri dolduruyoruz

        if(isActive.equals("active")){
            switch_btn.setChecked(true);
        }else{
            switch_btn.setChecked(false);
        }
        if(sound==1){
            isSound_btn.setChecked(true);

        }else{
            isSound_btn.setChecked(false);
        }
        if(vibrate==1){
            isVibrate_btn.setChecked(true);
        }else{
            isVibrate_btn.setChecked(false);
        }
        tv_topLimit.setText(String.valueOf(topLimit));
        tv_botLimit.setText(String.valueOf(botLimit));
    }
    void updateCounter(){
        //Veritabanındaki verileri güncelliyoruz
        topLimit=Integer.parseInt(tv_topLimit.getText().toString());
        botLimit=Integer.parseInt(tv_botLimit.getText().toString());
        isActive=switch_btn.isChecked()?"active":"nonactive";
        sound=isSound_btn.isChecked()?1:0;
        vibrate=isVibrate_btn.isChecked()?1:0;
        db.CounterUpdate(isActive,topLimit,botLimit,sound,vibrate);
    }

    void StoreDataList(){//veritabanından veri çekme
        Cursor cursor=db.GetCounter();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Hiç eski ayar yok", Toast.LENGTH_SHORT).show();// bu toast mesajına hiç bir zaman erişilemiyecek çünkü verilerimizi uygulama başlar başlamaz giriyoruz.
        }else{
            while(cursor.moveToNext()){
                isActive=cursor.getString(1);
                topLimit=cursor.getInt(2);
                botLimit=cursor.getInt(3);
                sound=cursor.getInt(4);
                vibrate=cursor.getInt(5);
            }
        }
    }
}
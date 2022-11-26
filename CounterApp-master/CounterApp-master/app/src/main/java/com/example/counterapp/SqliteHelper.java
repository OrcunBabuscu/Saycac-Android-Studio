package com.example.counterapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Counter";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_COUNTER = "counter";
    ////////////////////////////////////////////////////////////

    //////////////////////////////////////////////
    //id
    public static final String COUNTER_ID = "id";
    //COUNTER AKTİFLİĞİ
    public static final String COUNTER_ISACTIVE = "state";
    //COUNTER TOPLIMIT
    public static final String COUNTER_TOPLIMIT = "topLimit";
    //COUNTER BOTLIMIT
    public static final String COUNTER_BOTLIMIT = "botLimit";
    //COUNTER SOUND
    public static final String COUNTER_SOUND = "sound";
    //COUNTER VIBRATE
    public static final String COUNTER_VIBRATE = "vibrate";










    public static final String SQL_TABLE_COUNTER = " CREATE TABLE " + TABLE_COUNTER
            + " ( "
            + COUNTER_ID + " INTEGER PRIMARY KEY, "
            + COUNTER_ISACTIVE + " TEXT, "
            + COUNTER_TOPLIMIT + " INTEGER, "
            + COUNTER_BOTLIMIT + " INTEGER, "
            + COUNTER_SOUND + " INTEGER, "
            + COUNTER_VIBRATE + " INTEGER "
            + " ) ";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sql tablosunu oluşturan kod
        sqLiteDatabase.execSQL(SQL_TABLE_COUNTER);
        //Tabloya eleman eklediğimiz kod zaten tek bir elemanımız olucak ve sürekli olarak onu güncelleyeceğiz
        sqLiteDatabase.execSQL("insert into " + TABLE_COUNTER + " values(null,'active',100,0,1,1);");




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_COUNTER);




    }

    public void CounterUpdate(String isActive,int counter_top,int counter_bot,int counter_sound,int counter_vibrate){//Ayarların güncellendiği yer
        SQLiteDatabase db = this.getWritableDatabase();
        //tablonun sütun adı ve oraya gönderdiğimiz değeri veriyoruz ardından db.update ile güncellemeyi gerçekleştiriyoruz.
        ContentValues values = new ContentValues();
        values.put(COUNTER_ISACTIVE, isActive);//sutun adı , Girdiğimiz değer
        values.put(COUNTER_TOPLIMIT, counter_top);//column name, column value
        values.put(COUNTER_BOTLIMIT, counter_bot);//column name, column value
        values.put(COUNTER_SOUND, counter_sound);//column name, column value
        values.put(COUNTER_VIBRATE, counter_vibrate);//column name, column value

        // Inserting Row
        db.update(TABLE_COUNTER, values, COUNTER_ID + " = ?",
                new String[] { String.valueOf(1) });
        db.close(); // Closing database connection
    }
    public Cursor GetCounter(){//TÜM verileri çektiğimiz kısım
        String query="SELECT  * FROM " + TABLE_COUNTER;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }

}

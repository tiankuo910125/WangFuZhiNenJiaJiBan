package com.demo.smarthome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liukun on 2016/3/2.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smarthome.db";
    private final static int VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS config (id integer primary key autoincrement, domain integer, device text,time text, state text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS message (id integer primary key autoincrement, time text,title text, content text, type text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop if table exists config");
        db.execSQL("drop if table exists  message");
        onCreate(db);
    }
}

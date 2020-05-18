package com.anarchyghost.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.Nullable;

import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION= 1;
    public static final String DB_NAME= "NotesDB";
    public static final String TB_NAME= "Notes";

    public static final String KEY_ID= "_id";
    public static final String KEY_TYPE= "type";
    public static final String KEY_NAME= "name";
    public static final String KEY_TEXT= "text";
    public static final String KEY_DATE= "date";
    public static final String KEY_TIMEBEG= "time";
    public static final String KEY_TIMEEND= "timeend";
    public static final String KEY_ISDONE="isdone";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TB_NAME+"("+KEY_ID+" integer primary key,"+KEY_TYPE+" integer,"+
                KEY_NAME+" text,"+KEY_TEXT+" text,"+KEY_TIMEBEG+" integer,"+KEY_TIMEEND+" integer,"+KEY_DATE+" integer,"+KEY_ISDONE+" integer"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DB_NAME);
        onCreate(db);
    }

}
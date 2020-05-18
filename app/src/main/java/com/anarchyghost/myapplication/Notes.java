package com.anarchyghost.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anarchyghost.myapplication.db.DBHelper;

public class Notes {

    private final int id;
    private final String name;
    private final String  text;
    private final int type;
    private final long start;
    private final long end;
    private final long dead;
    private int isdone;
    public Notes(int id,String name, String text, int type, long start, long end, long dead,int isdone) {
        this.id=id;
        this.name = name;
        this.text = text;
        this.type = type;
        this.start = start;
        this.end = end;
        this.dead = dead;
        this.isdone=isdone;
    }
    public int getId(){return id;}
    public String getName(){
        return name;
    }

    public String getText(){
        return text;
    }

    public int getType(){
        return type;
    }

    public long getStart(){
        return start;
    }

    public long getEnd(){
        return end;
    }

    public long getDead(){
        return dead;
    }

    public void setIsdone(){if(isdone==0)isdone=1;else isdone=0;
    };

    public int getIsdone(){return this.isdone;}

}

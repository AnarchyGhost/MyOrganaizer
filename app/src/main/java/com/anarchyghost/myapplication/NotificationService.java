package com.anarchyghost.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.PRIORITY_HIGH;
import static com.anarchyghost.myapplication.NotesAdapter.TYPE_LESSON;
import static com.anarchyghost.myapplication.NotesAdapter.TYPE_TODO;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;

public class NotificationService extends Service {
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    SQLiteDatabase database;
    MyBinder binder = new MyBinder();
    Parser parser=new Parser();
    Timer timer;
    TimerTask tTask;
    long interval = 1000;
    Timer timer1;
    TimerTask tTask1;
    long interval1 = 1000;
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer1=new Timer();
        schedule();
        schedule1();
    }

    void schedule() {
        if (tTask != null) tTask.cancel();
            tTask = new TimerTask() {
                public void run() {
                    List<Notes> notes=new ArrayList<>();
                    dbHelper=new DBHelper(NotificationService.this);
                    database=dbHelper.getReadableDatabase();
                    Log.d("TIMER",""+parser.getDateTime());
                    Cursor cursor=null;
                        try {
                            cursor = database.query(DBHelper.TB_NAME, null, KEY_DATE +
                                    " = " + parser.parseToMillDate(parser.getCurDate()), null, null, null, KEY_TIMEBEG, null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    if(cursor.moveToFirst()){
                        int idIndex=cursor.getColumnIndex(DBHelper.KEY_ID);
                        int TypeIndex=cursor.getColumnIndex(DBHelper.KEY_TYPE);
                        int NameIndex=cursor.getColumnIndex(DBHelper.KEY_NAME);
                        int textIndex=cursor.getColumnIndex(DBHelper.KEY_TEXT);
                        int textStart=cursor.getColumnIndex(KEY_TIMEBEG);
                        int textEnd=cursor.getColumnIndex(DBHelper.KEY_TIMEEND);
                        int textDead=cursor.getColumnIndex(DBHelper.KEY_DATE);
                        int textIsdone=cursor.getColumnIndex(DBHelper.KEY_ISDONE);
                        do{
                            Notes notes1=new Notes(cursor.getInt(idIndex),cursor.getString(NameIndex),cursor.getString((textIndex)), cursor.getInt(TypeIndex),
                                    cursor.getLong(textStart), cursor.getLong(textEnd), cursor.getLong(textDead),cursor.getInt(textIsdone));

                            notes.add(notes1);
                        }while (cursor.moveToNext());
                    }else{
                        Log.d("mLog","0 rows");
                    }
                    for(int i=0;i<notes.size();i++){
                        try {
                            Notes note=notes.get(i);
                            if(note.getStart()-parser.parseToMillTime(parser.getDateTime())<=600000 && note.getStart()-parser.parseToMillTime(parser.getDateTime())>=0) {
                                Intent intent=new Intent(new Intent(getApplicationContext(), MainActivity.class));
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("id",note.getId());
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                switch (note.getType()) {
                                    case TYPE_TODO:
                                        NotificationCompat.Builder notificationBuilder =
                                            new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle("Дедлайн близко")
                                                    .setContentText(note.getName() + " Дедлайн:" + parser.parseToTime(note.getStart()))
                                                    .setPriority(PRIORITY_HIGH)
                                                    .setDefaults(Notification.DEFAULT_SOUND);

                                        createChannelIfNeeded(notificationManager);
                                        notificationManager.notify(note.getId(), notificationBuilder.build());
                                        break;
                                    case TYPE_LESSON:
                                        NotificationCompat.Builder notificationBuildere =
                                     notificationBuildere =
                                                new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                                                        .setAutoCancel(true)
                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                        .setSmallIcon(R.drawable.ic_school_black_24dp)
                                                        .setWhen(System.currentTimeMillis())
                                                        .setContentIntent(pendingIntent)
                                                        .setContentTitle("Скоро начинается пара")
                                                        .setContentText(note.getName() + " С " + parser.parseToTime(note.getStart())+" до "+parser.parseToTime(note.getEnd()))
                                                        .setPriority(PRIORITY_HIGH);
                                        createChannelIfNeeded(notificationManager);
                                        notificationManager.notify(note.getId(), notificationBuildere.build());
                                        break;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    database.close();
                    dbHelper.close();
                    }
            };

            timer.schedule(tTask, 1000, 600000);
    }
    void schedule1() {
        if (tTask1 != null) tTask1.cancel();
        tTask1 = new TimerTask() {
            public void run() {
                Log.d("TAG","Started");
                List<Birthday> birthdays =new ArrayList<>();
                dbHelper=new DBHelper(NotificationService.this);
                database=dbHelper.getReadableDatabase();
                Cursor cursor=null;
                try {
                    cursor = database.query(DBHelper.TB_BD_NAME, null, KEY_DATE +
                            " = " + parser.parseToMillMonth(parser.getDateMonth()), null, null, null, null, null);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(cursor.moveToFirst()){
                    int idIndex=cursor.getColumnIndex(DBHelper.KEY_ID);
                    int NameIndex=cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int textDead=cursor.getColumnIndex(DBHelper.KEY_DATE);
                    do{
                        Birthday birthday1 =new Birthday(cursor.getInt(idIndex),cursor.getString(NameIndex), cursor.getLong(textDead));

                        birthdays.add(birthday1);
                    }while (cursor.moveToNext());
                }else{
                    Log.d("mLog","0 rows");
                }
                for(int i = 0; i< birthdays.size(); i++){
                            Intent intent=new Intent(new Intent(getApplicationContext(), BirthdayActivity.class));
                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Birthday birthday = birthdays.get(i);
                                    NotificationCompat.Builder notificationBuilder =
                                            new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                                                    .setAutoCancel(true)
                                                    .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle("Сегодня день рождения!!!!")
                                                    .setContentText("День рождения у: "+birthday.getName() +", поздравь его!!")
                                                    .setPriority(PRIORITY_HIGH)
                                                    .setDefaults(Notification.DEFAULT_SOUND);
                                    createChannelIfNeeded(notificationManager);
                                    notificationManager.notify(birthday.getId()+10000, notificationBuilder.build());
                                    break;
                }
                database.close();
                dbHelper.close();
            }
        };

        timer1.schedule(tTask1, 1542, 432000000);
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_id", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "MyService onBind");
        return binder;
    }

    class MyBinder extends Binder {
        NotificationService getService() {
            return  NotificationService.this;
        }
    }
}

package com.anarchyghost.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.INVISIBLE;
import static com.anarchyghost.myapplication.NotesAdapter.TYPE_LESSON;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ISCONST;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ISDONE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEEND;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;
import static com.anarchyghost.myapplication.db.DBHelper.TB_CN_NAME;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class RaspisanieActivity extends AppCompatActivity {
    List<TextView> start = new ArrayList();
    List<TextView> end = new ArrayList();
    List<EditText> name = new ArrayList();
    TextView tv3;
    TextView tv1;
    TextView tv2;
    Button btn1;
    Button btn2;
    DBHelper dbHelper;
    List<ConstLessons> lessons;
    Parser parser = new Parser();
    DatePickerDialog.OnDateSetListener et_date_set;
    DatePickerDialog.OnDateSetListener et_date_end_set;
    TimePickerDialog.OnTimeSetListener et_beg_set;
    TimePickerDialog.OnTimeSetListener et_end_set;
    int c = 0;
    int t;
    int g;
    String tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            long date1=parser.parseToMillDate("2020-05-19");
            long date2=parser.parseToMillDate("2020-06-19");
        } catch (ParseException e) {


        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspisanie);
        start.add((TextView) findViewById(R.id.start_tv));
        start.add((TextView) findViewById(R.id.start_tv1));
        start.add((TextView) findViewById(R.id.start_tv2));
        start.add((TextView) findViewById(R.id.start_tv3));
        start.add((TextView) findViewById(R.id.start_tv4));
        start.add((TextView) findViewById(R.id.start_tv5));
        end.add((TextView) findViewById(R.id.end_tv));
        end.add((TextView) findViewById(R.id.end_tv1));
        end.add((TextView) findViewById(R.id.end_tv2));
        end.add((TextView) findViewById(R.id.end_tv3));
        end.add((TextView) findViewById(R.id.end_tv4));
        end.add((TextView) findViewById(R.id.end_tv5));
        name.add((EditText) findViewById(R.id.name_tv));
        name.add((EditText) findViewById(R.id.name_tv1));
        name.add((EditText) findViewById(R.id.name_tv2));
        name.add((EditText) findViewById(R.id.name_tv3));
        name.add((EditText) findViewById(R.id.name_tv4));
        name.add((EditText) findViewById(R.id.name_tv5));
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3=findViewById(R.id.textView);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        if (c == 0) btn1.setVisibility(INVISIBLE);
        btn1.setText("Предыдущий");
        btn2.setText("Следующий");
        dbHelper = new DBHelper(this);
        lessons = getNotes(dbHelper, c);

        stext();

        for (int i=0;i<6;i++){
            name.get(i).setHint("Название урока");
            try {
                start.get(i).setText(parser.parseToTime(parser.parseToMillTime("09:00")-parser.parseToMillTime("01:20")*i));
                end.get(i).setText(parser.parseToTime(parser.parseToMillTime("10:30")-parser.parseToMillTime("01:20")*i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < lessons.size(); i++) {

            start.get(i).setText(parser.parseToTime(lessons.get(i).getStart()));
            end.get(i).setText(parser.parseToTime(lessons.get(i).getEnd()));
            name.get(i).setText(lessons.get(i).getName());
        }
        for (int i=0;i<6;i++) {
            final int j = i;

            t = i;
            start.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int consta = j;
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    int sec = calendar.get(Calendar.SECOND);
                    TimePickerDialog dialog = new TimePickerDialog(RaspisanieActivity.this, R.style.Theme_AppCompat_Dialog, et_beg_set,
                            hour, minute, true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    Log.d("POS:", "POS:" + j);
                    t = j;
                }
            });
            g = i;
            end.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int consta = j;
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    int sec = calendar.get(Calendar.SECOND);
                    TimePickerDialog dialog = new TimePickerDialog(RaspisanieActivity.this, R.style.Theme_AppCompat_Dialog, et_end_set,
                            hour, minute, true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    Log.d("POS:", "POS:" + j);
                    g = j;
                }
            });
        }
        et_beg_set=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour = String.valueOf(hourOfDay);
                String nminute = String.valueOf(minute);
                if (hourOfDay < 10) nhour = "0" + nhour;
                if (minute < 10) nminute = "0" + nminute;
                String time = nhour + ":" + nminute;
                Log.d("TIME", time);
                tx = time;
                start.get(t).setText(time);
            }

        };
            et_end_set=new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String nhour = String.valueOf(hourOfDay);
                    String nminute = String.valueOf(minute);
                    if (hourOfDay < 10) nhour = "0" + nhour;
                    if (minute < 10) nminute = "0" + nminute;
                    String time = nhour + ":" + nminute;
                    Log.d("TIME", time);
                    tx = time;
                    end.get(g).setText(time);
                }

            };
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c != 6) {
                    if(c==0) {
                        btn1.setText("Предыдущее");
                        btn1.setVisibility(View.VISIBLE);
                    }
                    if(c!=6)btn2.setText("Следующее");
                    try {
                        addNotes(dbHelper);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c++;
                    stext();
                    lessons = getNotes(dbHelper, c);
                    for(int i=0;i<6;i++){
                        name.get(i).setHint("Название урока");
                        name.get(i).setText("");
                        try {
                            start.get(i).setText(parser.parseToTime(parser.parseToMillTime("09:00")-parser.parseToMillTime("01:20")*i));
                            end.get(i).setText(parser.parseToTime(parser.parseToMillTime("10:30")-parser.parseToMillTime("01:20")*i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < lessons.size(); i++) {
                        start.get(i).setText(parser.parseToTime(lessons.get(i).getStart()));
                        end.get(i).setText(parser.parseToTime(lessons.get(i).getEnd()));
                        name.get(i).setText(lessons.get(i).getName());
                    }
                    if(c==6)btn2.setText("Сохранить");
                } else {
                    try {
                        addNotes(dbHelper);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        setNotes(dbHelper,parser.parseToMillDate(tv1.getText().toString()),parser.parseToMillDate(tv2.getText().toString()));
                        finish();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        tv1.setText("УСТАНОВИТЕ ДАТУ!!!");
                        tv2.setText("УСТАНОВИТЕ ДАТУ!!!");
                    }
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNotes(dbHelper);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c--;
                for(int i=0;i<6;i++){
                    name.get(i).setText("");
                    name.get(i).setHint("Название урока");
                    try {
                        start.get(i).setText(parser.parseToTime(parser.parseToMillTime("09:00")-parser.parseToMillTime("01:20")*i));
                        end.get(i).setText(parser.parseToTime(parser.parseToMillTime("10:30")-parser.parseToMillTime("01:20")*i));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                lessons = getNotes(dbHelper, c);
                for (int i = 0; i < lessons.size(); i++) {
                    start.get(i).setText(parser.parseToTime(lessons.get(i).getStart()));
                    end.get(i).setText(parser.parseToTime(lessons.get(i).getEnd()));
                    name.get(i).setText(lessons.get(i).getName());
                }
                stext();

                if(c==0)btn1.setVisibility(INVISIBLE);

            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int dat=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RaspisanieActivity.this,R.style.Theme_AppCompat_Dialog , et_date_set,
                        year,month,dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int dat=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RaspisanieActivity.this,R.style.Theme_AppCompat_Dialog , et_date_end_set,
                        year,month,dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        et_date_set=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday=String.valueOf(dayOfMonth);
                month++;
                String nmonth= String.valueOf(month);

                if(dayOfMonth<10)nday="0"+dayOfMonth;
                if(month<10)nmonth="0"+String.valueOf(month);
                String date=year+"-"+nmonth+"-"+nday;
                tv1.setText(date);
            }
        };
        et_date_end_set=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday=String.valueOf(dayOfMonth);
                month++;
                String nmonth= String.valueOf(month);

                if(dayOfMonth<10)nday="0"+dayOfMonth;
                if(month<10)nmonth="0"+String.valueOf(month);
                String date=year+"-"+nmonth+"-"+nday;
                tv2.setText(date);
            }
        };
    }


    public List<ConstLessons> getNotes(DBHelper dbHelper, int day) {
        List<ConstLessons> notes = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = database.query(DBHelper.TB_CN_NAME, null, KEY_DATE +
                " = " + day, null, null, null, KEY_TIMEBEG, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int NameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textStart = cursor.getColumnIndex(KEY_TIMEBEG);
            int textEnd = cursor.getColumnIndex(DBHelper.KEY_TIMEEND);
            int textDead = cursor.getColumnIndex(DBHelper.KEY_DATE);
            do {
                ConstLessons notes1 = new ConstLessons(cursor.getInt(idIndex), cursor.getString(NameIndex),
                        cursor.getInt(textDead), cursor.getLong(textStart), cursor.getLong(textEnd));

                notes.add(notes1);
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }
        return notes;
    }

    public void addNotes(DBHelper dbHelper) throws ParseException {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(TB_CN_NAME, KEY_DATE + " = ?", new String[]{String.valueOf(c)});
        for (int i = 0; i < 6; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_NAME, name.get(i).getText().toString());
            contentValues.put(KEY_DATE, c);
            Log.d("M.tag",start.get(i).getText().toString());
            Log.d("M.tag",start.get(i).getText().toString());
            Log.d("M.tag",start.get(i).getText().toString());
            Log.d("M.tag",start.get(i).getText().toString());
            Log.d("M.tag",start.get(i).getText().toString());
            Log.d("M.tag",start.get(i).getText().toString());
            contentValues.put(KEY_TIMEBEG, parser.parseToMillTime(start.get(i).getText().toString()));
            contentValues.put(KEY_TIMEEND, parser.parseToMillTime(end.get(i).getText().toString()));
            if (!TextUtils.isEmpty(name.get(i).getText().toString()))
                database.insert(DBHelper.TB_CN_NAME, null, contentValues);
        }
    }

    public void setNotes(DBHelper dbHelper,long start,long end) {
        List<ConstLessons> lessons=new ArrayList<>();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long dw=start;
        database.delete(TB_NAME, KEY_ISCONST + " = ?", new String[]{String.valueOf(1)});
        for(int i=0;i<7;i++){
            if(i!=0)dw+=86400000;
            Log.d("DYOW",
                    ""+parser.parseToDayOfWeek(dw));
            lessons=getNotes(dbHelper,parser.parseToDayOfWeek(dw));
            for(long j=dw;j<=end;j+=86400000*7){
                for(int k=0;k<lessons.size();k++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, lessons.get(k).getName());
                contentValues.put(KEY_DATE, c);
                contentValues.put(KEY_ISCONST,1);
                contentValues.put(KEY_ISDONE,0);
                contentValues.put(KEY_DATE,j);
                contentValues.put(KEY_TYPE,TYPE_LESSON);

                contentValues.put(KEY_TIMEBEG, lessons.get(k).getStart());
                contentValues.put(KEY_TIMEEND, lessons.get(k).getEnd());
                if (!TextUtils.isEmpty(lessons.get(k).getName()))
                    database.insert(DBHelper.TB_NAME, null, contentValues);
            }
            }
        }

    }

    public void stext(){
        String day;
        switch (c){
            case 0:day="Понедельник";
            break;
            case 1:day="Вторник";
                break;
            case 2:day="Среда";
                break;
            case 3:day="Четверг";
                break;
            case 4:day="Пятница";
                break;
            case 5:day="Суббота";
                break;
            case 6:day="Воскресенье";
                break;
                default: day="ОШИБОЧКА ВЫШЛА";
        }
        tv3.setText(day);
    }

}

package com.anarchyghost.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdderTodoActivity extends AppCompatActivity {
    Button addbtn;

    TextView et_dead;
    TextView et_dead_time;
    EditText et_name;
    EditText et_text;
    DBHelper dbHelper;
    String dates;
    DatePickerDialog.OnDateSetListener et_dead_set;
    TimePickerDialog.OnTimeSetListener et_dead_settime;
    Parser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parser=new Parser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder_todo);
        Bundle info=getIntent().getExtras();
        dates=info.getString("date1");
        et_dead= findViewById(R.id.edit_deadline) ;
        et_name=(EditText)findViewById(R.id.edit_name) ;
        et_text=(EditText)findViewById(R.id.edit_text) ;
        et_dead_time=findViewById(R.id.edit_deadline_time);
        dbHelper=new DBHelper(this);
        et_dead.setText(dates);
        et_dead_time.setText(parser.getDateTime());

        et_dead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int dat=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdderTodoActivity.this,R.style.Theme_AppCompat_Dialog , et_dead_set,
                year,month,dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_dead_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour= calendar.get(Calendar.HOUR);
                int minute= calendar.get(Calendar.MINUTE);
                int sec=calendar.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(AdderTodoActivity.this,R.style.Theme_AppCompat_Dialog , et_dead_settime,
                       hour,minute,true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_dead_settime=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour=String.valueOf(hourOfDay);
                String nminute=String.valueOf(minute);
                if(hourOfDay<10)nhour="0"+nhour;
                if(minute<10)nminute="0"+nminute;
                String time=nhour+":"+nminute;
                et_dead_time.setText(time);
            }
        };
        et_dead_set=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday=String.valueOf(dayOfMonth);
                month++;
                String nmonth= String.valueOf(month);

                if(dayOfMonth<10)nday="0"+dayOfMonth;
                if(month<10)nmonth="0"+String.valueOf(month);
                String date=year+"-"+nmonth+"-"+nday;
                et_dead.setText(date);
            }
        };
        addbtn = findViewById(R.id.button5);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String text = et_text.getText().toString();
                if(!TextUtils.isEmpty(name)) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_NAME, name);
                    contentValues.put(DBHelper.KEY_TEXT, text);
                    try {
                        contentValues.put(DBHelper.KEY_DATE, parser.parseToMillDate(et_dead.getText().toString()));
                        contentValues.put(DBHelper.KEY_TIMEBEG, parser.parseToMillTime(et_dead_time.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    contentValues.put(DBHelper.KEY_TYPE, 2);
                    contentValues.put(DBHelper.KEY_ISDONE, 0);
                    database.insert(DBHelper.TB_NAME, null, contentValues);

                    finish();
                }
            }
        });
    }
}

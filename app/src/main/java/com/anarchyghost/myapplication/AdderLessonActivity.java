package com.anarchyghost.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.util.Calendar;

public class AdderLessonActivity extends AppCompatActivity {
Button addbtn;
TextView et_name;
TextView et_start;
TextView et_date;
TextView et_end;
DBHelper dbHelper;
String dates;
DatePickerDialog.OnDateSetListener et_date_set;
TimePickerDialog.OnTimeSetListener et_beg_set;
TimePickerDialog.OnTimeSetListener et_end_set;
Parser parser=new Parser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder_lesson);

        Bundle info=getIntent().getExtras();
        dates=info.getString("date1");



        et_name=(EditText)findViewById(R.id.edit_name) ;
        et_date=findViewById(R.id.date);
        et_start=findViewById(R.id.edit_timeofbeg);
        et_end=findViewById(R.id.edit_timeofen);

        et_date.setText(dates);
        et_start.setText(parser.getDateTime());
        try {
            et_end.setText(parser.parseToTime(parser.parseToMillTime(parser.getDateTime())+5400000));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int dat=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdderLessonActivity.this,R.style.Theme_AppCompat_Dialog , et_date_set,
                        year,month,dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour= calendar.get(Calendar.HOUR);
                int minute= calendar.get(Calendar.MINUTE);
                int sec=calendar.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(AdderLessonActivity.this,R.style.Theme_AppCompat_Dialog , et_beg_set,
                        hour,minute,true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour= calendar.get(Calendar.HOUR);
                int minute= calendar.get(Calendar.MINUTE);
                int sec=calendar.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(AdderLessonActivity.this,R.style.Theme_AppCompat_Dialog , et_beg_set,
                        hour,minute,true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_beg_set=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour=String.valueOf(hourOfDay);
                String nminute=String.valueOf(minute);
                if(hourOfDay<10)nhour="0"+nhour;
                if(minute<10)nminute="0"+nminute;
                String time=nhour+":"+nminute;
                et_start.setText(time);
            }
        };

        et_date_set=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday=String.valueOf(dayOfMonth);
                month++;
                String nmonth= String.valueOf(month);

                if(dayOfMonth<10)nday="0"+dayOfMonth;
                if(month<10)nmonth="0"+String.valueOf(month);
                String date=year+"-"+nmonth+"-"+nday;
                et_date.setText(date);
            }
        };



        addbtn = findViewById(R.id.button5);
        dbHelper=new DBHelper(this);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                if(!TextUtils.isEmpty(name)) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_NAME, name);
                    try {
                        contentValues.put(DBHelper.KEY_TIMEBEG, parser.parseToMillTime(et_start.getText().toString()));
                        contentValues.put(DBHelper.KEY_TIMEEND, parser.parseToMillTime(et_end.getText().toString()));
                        contentValues.put(DBHelper.KEY_DATE, parser.parseToMillDate(et_date.getText().toString()));
                        contentValues.put(DBHelper.KEY_ISDONE, 0);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    contentValues.put(DBHelper.KEY_TYPE, 0);

                    database.insert(DBHelper.TB_NAME, null, contentValues);

                    finish();
                }else {
                    et_name.setHint("Введите название урока");
                }
            }
        });
    }
}
